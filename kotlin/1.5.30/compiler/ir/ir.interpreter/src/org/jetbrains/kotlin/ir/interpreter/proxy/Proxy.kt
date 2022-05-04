/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.interpreter.proxy

import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.interpreter.CallInterceptor
import org.jetbrains.kotlin.ir.interpreter.isPrimitiveArray
import org.jetbrains.kotlin.ir.interpreter.proxy.CommonProxy.Companion.asProxy
import org.jetbrains.kotlin.ir.interpreter.proxy.reflection.ReflectionProxy.Companion.asProxy
import org.jetbrains.kotlin.ir.interpreter.state.*
import org.jetbrains.kotlin.ir.interpreter.state.reflection.ReflectionState
import org.jetbrains.kotlin.ir.types.isArray
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import java.lang.invoke.MethodType

internal interface Proxy {
    val state: State
    val callInterceptor: CallInterceptor

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    override fun toString(): String
}

internal fun State.wrap(callInterceptor: CallInterceptor, remainArraysAsIs: Boolean, extendFrom: Class<*>? = null): Any? {
    return when (this) {
        is ExceptionState -> this
        is Wrapper -> this.value
        is Primitive<*> -> when {
            this.type.isArray() || this.type.isPrimitiveArray() -> if (remainArraysAsIs) this else this.value
            else -> this.value
        }
        is Common -> this.asProxy(callInterceptor, extendFrom)
        is ReflectionState -> this.asProxy(callInterceptor)
        else -> throw AssertionError("${this::class} is unsupported as argument for wrap function")
    }
}

/**
 * Prepare state object to be passed in outer world
 */
internal fun List<State>.wrap(callInterceptor: CallInterceptor, irFunction: IrFunction, methodType: MethodType? = null): List<Any?> {
    val name = irFunction.fqNameWhenAvailable?.asString()
    return this.mapIndexed { index, state ->
        // don't get arrays from Primitive in case of "set" and "Pair.<init>"; information about type will be lost
        val unwrapArrays = name?.let { (it == "kotlin.Array.set" && index != 0) || it == "kotlin.Pair.<init>" } ?: false
        state.wrap(callInterceptor, unwrapArrays, methodType?.parameterType(index))
    }
}

internal fun Class<*>.isObject(): Boolean {
    return this == java.lang.Object::class.java
}