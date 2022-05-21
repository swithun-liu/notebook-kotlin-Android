/**
 * [remotepath](https://github.com/swithun-liu/notebook-kotlin-Android/blob/master/kotlin/1.5.30/libraries/stdlib/src/kotlin/coroutines/intrinsics/Intrinsics.kt)
 */

/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:kotlin.jvm.JvmName("IntrinsicsKt")
@file:kotlin.jvm.JvmMultifileClass

package kotlin.coroutines.intrinsics

import kotlin.contracts.*
import kotlin.coroutines.*
import kotlin.internal.InlineOnly

/**
 * Obtains the current continuation instance inside suspend functions and either suspends
 * currently running coroutine or returns result immediately without suspension.
 *
 * If the [block] returns the special [COROUTINE_SUSPENDED] value, it means that suspend function did suspend the execution and will
 * not return any result immediately. In this case, the [Continuation] provided to the [block] shall be
 * resumed by invoking [Continuation.resumeWith] at some moment in the
 * future when the result becomes available to resume the computation.
 *
 * Otherwise, the return value of the [block] must have a type assignable to [T] and represents the result of this suspend function.
 * It means that the execution was not suspended and the [Continuation] provided to the [block] shall not be invoked.
 * As the result type of the [block] is declared as `Any?` and cannot be correctly type-checked,
 * its proper return type remains on the conscience of the suspend function's author.
 *
 * Invocation of [Continuation.resumeWith] resumes coroutine directly in the invoker's thread without going through the
 * [ContinuationInterceptor] that might be present in the coroutine's [CoroutineContext].
 * It is the invoker's responsibility to ensure that a proper invocation context is established.
 * [Continuation.intercepted] can be used to acquire the intercepted continuation.
 *
 * Note that it is not recommended to call either [Continuation.resume] nor [Continuation.resumeWithException] functions synchronously
 * in the same stackframe where suspension function is run. Use [suspendCoroutine] as a safer way to obtain current
 * continuation instance.
 */
/**
 * swithun-note
 * 获取当前在suspend function中的continuation实例，
 * 会挂起当前 coroutine 或者立即返回结果。
 * 
 * 如果block返回COROUTINE_SUSPENDED，则表示suspend function 确实挂起了执行 并且 不会立即返回结果。
 * 在这种情况下，block提供的 [Continuation] 实例在之后会通过执行 [Continuation.resumeWith] 来恢复执行当result可用时。
 * 
 * 否则，block返回的结果必须是T类型的，并且表示suspend function的执行结果。
 * 者意味着执行并没有被挂起，并且 [blocl] 提供的 [Continuation] 实例不会被调用。
 * 因为block的返回类型是Any?，所以不能正确的类型检查，suspend function的作者的责任是保持这个返回类型的正确性。
 * 
 * [Continuation.resumeWith] 立即恢复执行的coroutine，不会经过可能在 coroutine 的[CoroutineContext]中存在的 [ContinuationInterceptor]。
 * 一个正确的调用 context 的建立责任是被invoker负责。
 * [Continuation.intercepted] 可以获取被拦截的continuation。
 * 
 * 注意，不建议在相同的栈帧中在suspend function中直接同步地调用调用 [Continuation.resume] 或者 [Continuation.resumeWithException] ，
 * 使用 [suspendCoroutine] 作为一个更安全的方式来获取当前continuation实例。
 */
@SinceKotlin("1.3")
@InlineOnly
@Suppress("UNUSED_PARAMETER", "RedundantSuspendModifier")
public suspend inline fun <T> suspendCoroutineUninterceptedOrReturn(crossinline block: (Continuation<T>) -> Any?): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    throw NotImplementedError("Implementation of suspendCoroutineUninterceptedOrReturn is intrinsic")
}

/**
 * This value is used as a return value of [suspendCoroutineUninterceptedOrReturn] `block` argument to state that
 * the execution was suspended and will not return any result immediately.
 *
 * **Note: this value should not be used in general code.** Using it outside of the context of
 * `suspendCoroutineUninterceptedOrReturn` function return value  (including, but not limited to,
 * storing this value in other properties, returning it from other functions, etc)
 * can lead to unspecified behavior of the code.
 */
// It is implemented as property with getter to avoid ProGuard <clinit> problem with multifile IntrinsicsKt class
@SinceKotlin("1.3")
public val COROUTINE_SUSPENDED: Any get() = CoroutineSingletons.COROUTINE_SUSPENDED

// Using enum here ensures two important properties:
//  1. It makes SafeContinuation serializable with all kinds of serialization frameworks (since all of them natively support enums)
//  2. It improves debugging experience, since you clearly see toString() value of those objects and what package they come from
@SinceKotlin("1.3")
@PublishedApi // This class is Published API via serialized representation of SafeContinuation, don't rename/move
internal enum class CoroutineSingletons { COROUTINE_SUSPENDED, UNDECIDED, RESUMED }
