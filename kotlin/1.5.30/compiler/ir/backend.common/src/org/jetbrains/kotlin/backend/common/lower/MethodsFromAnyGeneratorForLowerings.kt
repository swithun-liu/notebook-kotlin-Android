/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common.lower

import org.jetbrains.kotlin.backend.common.BackendContext
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.TypeParameterDescriptor
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addFunction
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.isArray
import org.jetbrains.kotlin.ir.util.DataClassMembersGenerator
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.isPrimitiveArray

class MethodsFromAnyGeneratorForLowerings(val context: BackendContext, val irClass: IrClass, val origin: IrDeclarationOrigin) {
    fun createToStringMethodDeclaration(): IrSimpleFunction = irClass.addFunction("toString", context.irBuiltIns.stringType).apply {
        overriddenSymbols = irClass.collectOverridenSymbols { it.isToString() }
    }

    fun createHashCodeMethodDeclaration(): IrSimpleFunction = irClass.addFunction("hashCode", context.irBuiltIns.intType).apply {
        overriddenSymbols = irClass.collectOverridenSymbols { it.isHashCode() }
    }

    fun createEqualsMethodDeclaration(): IrSimpleFunction = irClass.addFunction("equals", context.irBuiltIns.booleanType).apply {
        overriddenSymbols = irClass.collectOverridenSymbols { it.isEquals(context) }
        addValueParameter("other", context.irBuiltIns.anyNType)
    }

    inner class LoweringDataClassMemberGenerator(
        val nameForToString: String,
        val typeForEquals: IrType,
        val selectEquals: IrBlockBodyBuilder.(IrType, IrExpression, IrExpression) -> IrExpression,
    ) :
        DataClassMembersGenerator(
            IrLoweringContext(context),
            context.ir.symbols.externalSymbolTable,
            irClass,
            origin
        ) {

        override fun declareSimpleFunction(startOffset: Int, endOffset: Int, functionDescriptor: FunctionDescriptor): IrFunction {
            error("Descriptor API shouldn't be used in lowerings")
        }

        override fun generateSyntheticFunctionParameterDeclarations(irFunction: IrFunction) {
            // no-op — irFunction from lowering should already have necessary parameters
        }

        override fun getProperty(parameter: ValueParameterDescriptor?, irValueParameter: IrValueParameter?): IrProperty? {
            error("Descriptor API shouldn't be used in lowerings")
        }

        override fun transform(typeParameterDescriptor: TypeParameterDescriptor): IrType {
            error("Descriptor API shouldn't be used in lowerings")
        }

        override val intPlusSymbol: IrSimpleFunctionSymbol =
            context.irBuiltIns.intClass.functions.single { it.owner.name.asString() == "plus" && it.owner.valueParameters[0].type == context.irBuiltIns.intType }

        override val intTimesSymbol: IrSimpleFunctionSymbol =
            context.irBuiltIns.intClass.functions.single { it.owner.name.asString() == "times" && it.owner.valueParameters[0].type == context.irBuiltIns.intType }

        override fun getHashCodeFunctionInfo(type: IrType): HashCodeFunctionInfo {
            val symbol = if (type.isArray() || type.isPrimitiveArray()) {
                context.irBuiltIns.dataClassArrayMemberHashCodeSymbol
            } else {
                context.irBuiltIns.anyClass.functions.single { it.owner.name.asString() == "hashCode" }
            }
            return object : HashCodeFunctionInfo {
                override val symbol: IrSimpleFunctionSymbol = symbol

                override fun commitSubstituted(irMemberAccessExpression: IrMemberAccessExpression<*>) {}
            }
        }

        override fun IrClass.classNameForToString(): String = nameForToString

        fun generateEqualsUsingGetters(equalsFun: IrSimpleFunction, typeForEquals: IrType, properties: List<IrProperty>) = equalsFun.apply {
            body = this@MethodsFromAnyGeneratorForLowerings.context.createIrBuilder(symbol).irBlockBody {
                val irType = typeForEquals
                fun irOther() = irGet(valueParameters[0])
                fun irThis() = irGet(dispatchReceiverParameter!!)
                fun IrProperty.get(receiver: IrExpression) = irCall(getter!!).apply {
                    dispatchReceiver = receiver
                }

                +irIfThenReturnFalse(irNotIs(irOther(), irType))
                val otherWithCast = irTemporary(irAs(irOther(), irType), "other_with_cast")
                for (property in properties) {
                    val arg1 = property.get(irThis())
                    val arg2 = property.get(irGet(irType, otherWithCast.symbol))
                    +irIfThenReturnFalse(irNot(selectEquals(property.getter?.returnType ?: property.backingField!!.type, arg1, arg2)))
                }
                +irReturnTrue()
            }
        }
    }

    companion object {
        fun IrFunction.isToString(): Boolean =
            name.asString() == "toString" && extensionReceiverParameter == null && valueParameters.isEmpty()

        fun IrFunction.isHashCode() =
            name.asString() == "hashCode" && extensionReceiverParameter == null && valueParameters.isEmpty()

        fun IrFunction.isEquals(context: BackendContext) =
            name.asString() == "equals" &&
                    extensionReceiverParameter == null &&
                    valueParameters.singleOrNull()?.type == context.irBuiltIns.anyNType


        fun IrClass.collectOverridenSymbols(predicate: (IrFunction) -> Boolean): List<IrSimpleFunctionSymbol> =
            superTypes.mapNotNull { it.getClass()?.functions?.singleOrNull(predicate)?.symbol }

    }
}