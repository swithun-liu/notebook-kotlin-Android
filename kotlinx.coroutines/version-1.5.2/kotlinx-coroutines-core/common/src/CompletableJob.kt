/*
 * Copyright 2016-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines

/**
 * A job that can be completed using [complete()] function.
 * It is returned by [Job()][Job] and [SupervisorJob()][SupervisorJob] constructor functions.
 *
 * All functions on this interface are **thread-safe** and can
 * be safely invoked from concurrent coroutines without external synchronization.
 *
 * **The `CompletableJob` interface is not stable for inheritance in 3rd party libraries**,
 * as new methods might be added to this interface in the future, but is stable for use.
 */
/**
 * swithun-note
 * 一个能被通过调用[complete()]完成的job。
 * 它是[Job()][Job]和[SupervisorJob()][SupervisorJob]构造函数返回的。
 * 
 * 这个借口的所有函数是 **线程安全**的，并且可以无需外部同步的情况下被并发的协程调用。
 * 
 * **对于第三方库得继承来说，`CompletableJob`是不稳定的，因为可能回增加新的方法，但是对于使用来说是稳定的**。
 */
public interface CompletableJob : Job {
    /**
     * Completes this job. The result is `true` if this job was completed as a result of this invocation and
     * `false` otherwise (if it was already completed).
     *
     * Subsequent invocations of this function have no effect and always produce `false`.
     *
     * This function transitions this job into _completed_ state if it was not completed or cancelled yet.
     * However, that if this job has children, then it transitions into _completing_ state and becomes _complete_
     * once all its children are [complete][isCompleted]. See [Job] for details.
     */
    public fun complete(): Boolean

    /**
     * Completes this job exceptionally with a given [exception]. The result is `true` if this job was
     * completed as a result of this invocation and `false` otherwise (if it was already completed).
     * [exception] parameter is used as an additional debug information that is not handled by any exception handlers.
     *
     * Subsequent invocations of this function have no effect and always produce `false`.
     *
     * This function transitions this job into _cancelled_ state if it was not completed or cancelled yet.
     * However, that if this job has children, then it transitions into _cancelling_ state and becomes _cancelled_
     * once all its children are [complete][isCompleted]. See [Job] for details.
     *
     * Its responsibility of the caller to properly handle and report the given [exception], all job's children will receive
     * a [CancellationException] with the [exception] as a cause for the sake of diagnostic.
     */
    public fun completeExceptionally(exception: Throwable): Boolean
}
