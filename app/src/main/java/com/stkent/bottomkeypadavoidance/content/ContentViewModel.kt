package com.stkent.bottomkeypadavoidance.content

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stkent.bottomkeypadavoidance.*
import com.stkent.bottomkeypadavoidance.Selection.None
import com.stkent.bottomkeypadavoidance.Selection.Some
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.onEach

class ContentViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(ContentViewState(emptyList()))
    val viewState: Flow<ContentViewState> = _viewState

    private val _scrollToIndex = Channel<Int>()
    val scrollToIndex: ReceiveChannel<Int> = _scrollToIndex

    private lateinit var _cachedMainState: MainState

    init {
        MainObject.state
            .onEach { state -> _cachedMainState = state }
            .launchIn(viewModelScope)

        MainObject.state
            .onEach { Log.d(LOG_TAG, "ContentViewModel received new MainState.") }
            .withLast()
            .onEach { (oldMainState, newMainState) ->
                _viewState.value = ContentViewState.from(newMainState)
                performSideEffects(old = oldMainState, new = newMainState)
            }
            .launchIn(viewModelScope)
    }

    fun onItemSelected(int: Int) {
        Log.d(LOG_TAG, "ContentViewModel::onItemSelected")
        select(Some(int))
    }

    fun onBackPressed() {
        Log.d(LOG_TAG, "ContentViewModel::onBackPressed")
        select(None)
    }

    fun onScroll() {
        Log.d(LOG_TAG, "ContentViewModel::onScroll")
        if (_cachedMainState.selection !is None) select(None)
    }

    private fun performSideEffects(old: MainState?, new: MainState) {
        val selectedNewInt = new.selection != None && old?.selection == None

        val changedSelectedInt =
            new.selection != None && old?.selection != None && new.selection != old?.selection

        val scrollIndex = new.selectedIndex

        when {
            selectedNewInt -> {
                Log.v(LOG_TAG, "ContentViewModel emitting new scrollToIndex: ${scrollIndex!!}.")
                _scrollToIndex.offer(scrollIndex)
            }

            changedSelectedInt -> _scrollToIndex.offer(scrollIndex!!)
        }
    }

    private fun select(selection: Selection) {
        Log.d(LOG_TAG, "ContentViewModel::select($selection)")
        MainObject.select(selection)
    }

}
