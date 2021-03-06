/*
 * Copyright 2016-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */
@file:OptIn(ExperimentalContracts::class)
@file:Suppress("DEPRECATION_ERROR")

package kotlinx.coroutines

import kotlinx.coroutines.internal.*
import kotlinx.coroutines.intrinsics.*
import kotlin.contracts.*
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*
import kotlin.jvm.*

/**
 * Creates a _supervisor_ job object in an active state.
 * Children of a supervisor job can fail independently of each other.
 * 
 * A failure or cancellation of a child does not cause the supervisor job to fail and does not affect its other children,
 * so a supervisor can implement a custom policy for handling failures of its children:
 *
 * * A failure of a child job that was created using [launch][CoroutineScope.launch] can be handled via [CoroutineExceptionHandler] in the context.
 * * A failure of a child job that was created using [async][CoroutineScope.async] can be handled via [Deferred.await] on the resulting deferred value.
 *
 * If [parent] job is specified, then this supervisor job becomes a child job of its parent and is cancelled when its
 * parent fails or is cancelled. All this supervisor's children are cancelled in this case, too. The invocation of
 * [cancel][Job.cancel] with exception (other than [CancellationException]) on this supervisor job also cancels parent.
 *
 * @param parent an optional parent job.
 */

/**
 * swithun-note
 * 创建一个_supervisor_job 对象在活动状态。
 * supervisor job的子job可以失败独立于其他子job。
 * 
 * 一个child的失败不会导致supervisor job失败，并切不影响其他的children，所以supervisor可以实现自定义处理子job的失败：
 * 
 * * 一个使用[launch][CoroutineScope.launch]创建的child job失败可以通过[CoroutineExceptionHandler]在上下文中处理。
 * * 一个使用[async][CoroutineScope.async]创建的child job失败可以通过[Deferred.await]获取到产生的deferred值。
 * 
 * 如果制定了[parent] job，那么这个supervisor job会成为其父job的子job，当其父job失败或被取消时，这个supervisor job(包括他的子job)也会被取消。
 * 带有exception(除了[CancellationException])的[cancel][Job.cancel]调用也会取消父job。
 * 
 * @param parent 一个可选的父job。
 */
@Suppress("FunctionName")
public fun SupervisorJob(parent: Job? = null) : CompletableJob = SupervisorJobImpl(parent)

/** @suppress Binary compatibility only */
@Suppress("FunctionName")
@Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
@JvmName("SupervisorJob")
public fun SupervisorJob0(parent: Job? = null) : Job = SupervisorJob(parent)

/**
 * Creates a [CoroutineScope] with [SupervisorJob] and calls the specified suspend block with this scope.
 * The provided scope inherits its [coroutineContext][CoroutineScope.coroutineContext] from the outer scope, but overrides
 * context's [Job] with [SupervisorJob].
 * This function returns as soon as the given block and all its child coroutines are completed.
 *
 * Unlike [coroutineScope], a failure of a child does not cause this scope to fail and does not affect its other children,
 * so a custom policy for handling failures of its children can be implemented. See [SupervisorJob] for additional details.
 * A failure of the scope itself (exception thrown in the [block] or external cancellation) fails the scope with all its children,
 * but does not cancel parent job.
 *
 * The method may throw a [CancellationException] if the current job was cancelled externally,
 * or rethrow an exception thrown by the given [block].
 */
public suspend fun <R> supervisorScope(block: suspend CoroutineScope.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return suspendCoroutineUninterceptedOrReturn { uCont ->
        val coroutine = SupervisorCoroutine(uCont.context, uCont)
        coroutine.startUndispatchedOrReturn(coroutine, block)
    }
}

private class SupervisorJobImpl(parent: Job?) : JobImpl(parent) {
    override fun childCancelled(cause: Throwable): Boolean = false
}

private class SupervisorCoroutine<in T>(
    context: CoroutineContext,
    uCont: Continuation<T>
) : ScopeCoroutine<T>(context, uCont) {
    override fun childCancelled(cause: Throwable): Boolean = false
}
