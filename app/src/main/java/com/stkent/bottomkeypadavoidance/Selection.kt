package com.stkent.bottomkeypadavoidance

sealed class Selection {
    data class Some(val value: Int) : Selection()
    object None : Selection()
}
