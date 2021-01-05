package com.stkent.bottomkeypadavoidance

import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.stkent.bottomkeypadavoidance.databinding.ActivityMainBinding
import com.stkent.bottomkeypadavoidance.nav.NavMode
import com.stkent.bottomkeypadavoidance.nav.NavState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.withIndex

class MainActivity : AppCompatActivity() {

    private val controlsTransition = TransitionSet().apply {
        addTransition(ChangeBounds())
        duration = 100
        interpolator = AccelerateDecelerateInterpolator()
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavState.initConstraints(binding.root)

        viewModel.navMode
            .onEach { Log.d(LOG_TAG, "MainActivity received new NavMode.") }
            .map { navMode ->
                when (navMode) {
                    NavMode.Keypad -> NavState.Keypad
                    NavMode.None -> NavState.None
                }
            }
            .withIndex()
            .onEach { (index, navState) ->
                if (index > 0) {
                    delay(50)
                }

                render(navState)
            }
            .launchIn(lifecycle.coroutineScope)
    }

    private fun render(navState: NavState) {
        Log.d(LOG_TAG, "MainActivity rendering new NavState.")

        val constraintLayout = binding.root
        navState.constraintSet.applyTo(constraintLayout)
        TransitionManager.beginDelayedTransition(constraintLayout, controlsTransition)

        Log.d(LOG_TAG, "MainActivity animating constraint changes.")
    }

}
