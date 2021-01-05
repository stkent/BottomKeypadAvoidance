package com.stkent.bottomkeypadavoidance.content

import com.stkent.bottomkeypadavoidance.MainState
import com.stkent.bottomkeypadavoidance.Selection

data class ContentViewState(val data: List<SelectableInt>) {

    companion object {
        fun from(mainState: MainState): ContentViewState {
            return ContentViewState(
                data = mainState.ints
                    .map { int ->
                        val selected = when (val selection = mainState.selection) {
                            is Selection.Some -> int == selection.value
                            Selection.None -> false
                        }

                        SelectableInt(value = int, selected = selected)
                    }
            )
        }
    }

}
