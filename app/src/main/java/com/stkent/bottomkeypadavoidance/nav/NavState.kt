package com.stkent.bottomkeypadavoidance.nav

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import com.stkent.bottomkeypadavoidance.R

enum class NavState(val constraintSet: ConstraintSet) {

    Keypad(ConstraintSet()),

    None(ConstraintSet());

    companion object {
        fun initConstraints(root: ConstraintLayout) {
            Keypad.constraintSet.apply {
                clone(root)

                R.id.keypad_container.let {
                    connect(it, BOTTOM, PARENT_ID, BOTTOM)
                    clear(it, TOP)
                }

                connect(R.id.content_container, BOTTOM, R.id.keypad_container, TOP)
            }

            None.constraintSet.apply {
                clone(root)

                R.id.keypad_container.let {
                    connect(it, TOP, PARENT_ID, BOTTOM)
                    clear(it, BOTTOM)
                }

                connect(R.id.content_container, BOTTOM, PARENT_ID, BOTTOM)
            }
        }
    }
}
