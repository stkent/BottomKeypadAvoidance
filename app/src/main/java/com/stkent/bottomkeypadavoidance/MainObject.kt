package com.stkent.bottomkeypadavoidance

import android.util.Log
import com.stkent.bottomkeypadavoidance.Selection.None
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach

object MainObject {

    @Suppress("ObjectPropertyName")
    private val _state = MutableStateFlow(MainState((0 until 40).toList(), None))
    val state: Flow<MainState> = _state
        .onEach { Log.d(LOG_TAG, "BudgetTab::state emitting new value.") }

    fun select(selection: Selection) {
        _state.value = _state.value.copy(selection = selection)
    }

}
