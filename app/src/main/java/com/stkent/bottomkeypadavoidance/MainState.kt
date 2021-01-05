package com.stkent.bottomkeypadavoidance

import com.stkent.bottomkeypadavoidance.Selection.None
import com.stkent.bottomkeypadavoidance.Selection.Some

data class MainState(val ints: List<Int>, val selection: Selection) {

    val selectedIndex: Int?
        get() = when (selection) {
            is Some -> ints
                .withIndex()
                .find { (_, int) -> int == selection.value }
                ?.index

            None -> null
        }

}
