/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.util

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrScriptSymbol
import org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classifierOrFail
import org.jetbrains.kotlin.ir.types.isMarkedNullable

/**
 * Returns inline class for given class or null of type is not inlined
 * TODO: Make this configurable for different backends (currently implements logic of JS BE)
 */
private fun IrType.getInlinedClass(): IrClass? {
    if (this is IrSimpleType) {
        val erased = erase(this) ?: return null
        if (erased.isInline) {
            if (this.isMarkedNullable()) {
                var fieldType: IrType
                var fieldInlinedClass = erased
                while (true) {
                    fieldType = getInlineClassUnderlyingType(fieldInlinedClass)
                    if (fieldType.isMarkedNullable()) {
                        return null
                    }

                    fieldInlinedClass = fieldType.getInlinedClass() ?: break
                }
            }

            return erased
        }
    }
    return null
}

/**
 * Do not use this function in the JVM backend! Use `isInlineClassType` instead.
 *
 * This function has slightly different semantics for generic type parameters with inline class bounds.
 *
 * TODO: examine remaining usages in Native and preferably remove this function.
 */
fun IrType.isInlined(): Boolean = this.getInlinedClass() != null

private tailrec fun erase(type: IrType): IrClass? {
    val classifier = type.classifierOrFail

    return when (classifier) {
        is IrClassSymbol -> classifier.owner
        is IrScriptSymbol -> null // TODO: check if correct
        is IrTypeParameterSymbol -> erase(classifier.owner.superTypes.first())
        else -> error(classifier)
    }
}

fun getInlineClassUnderlyingType(irClass: IrClass): IrSimpleType {
    val representation = irClass.inlineClassRepresentation ?: error("Not an inline class: ${irClass.render()}")
    return representation.underlyingType
}

fun getInlineClassBackingField(irClass: IrClass): IrField {
    for (declaration in irClass.declarations) {
        if (declaration is IrField && !declaration.isStatic)
            return declaration

        if (declaration is IrProperty) {
            val backingField = declaration.backingField
            if (backingField != null && !backingField.isStatic) {
                return backingField
            }
        }
    }
    error("Inline class has no field: ${irClass.fqNameWhenAvailable}")
}
