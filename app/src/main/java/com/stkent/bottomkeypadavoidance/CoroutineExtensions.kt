package com.stkent.bottomkeypadavoidance

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

/**
 * Emits values from the receiving flow paired with {the previous value emitted by the receiving
 * flow, if it exists, or `null` otherwise}.
 *
 * Inspired by https://github.com/Kotlin/kotlinx.coroutines/pull/1558.
 */
fun <T : Any> Flow<T>.withLast(): Flow<Pair<T?, T>> {
    return flow {
        var last: T? = null

        collect { value ->
            emit(last to value)
            last = value
        }
    }
}
