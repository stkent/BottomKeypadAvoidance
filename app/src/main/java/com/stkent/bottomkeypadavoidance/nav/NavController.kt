package com.stkent.bottomkeypadavoidance.nav

import android.util.Log
import com.stkent.bottomkeypadavoidance.LOG_TAG
import com.stkent.bottomkeypadavoidance.MainObject
import com.stkent.bottomkeypadavoidance.Selection
import com.stkent.bottomkeypadavoidance.nav.NavMode.Keypad
import com.stkent.bottomkeypadavoidance.nav.NavMode.None
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

object NavController {

    val navMode: Flow<NavMode> =
        MainObject.state
            .onEach { Log.d(LOG_TAG, "NavController received new MainState.") }
            .map { state ->
                when (state.selection) {
                    Selection.None -> None
                    is Selection.Some -> Keypad
                }
            }
            .onStart { emit(None) }

}
