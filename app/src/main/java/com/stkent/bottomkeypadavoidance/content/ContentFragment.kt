package com.stkent.bottomkeypadavoidance.content

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stkent.bottomkeypadavoidance.LOG_TAG
import com.stkent.bottomkeypadavoidance.databinding.FragmentContentBinding
import kotlinx.coroutines.flow.collect

class ContentFragment : Fragment() {

    private val viewModel: ContentViewModel by activityViewModels()

    private var _binding: FragmentContentBinding? = null
    private val binding: FragmentContentBinding
        get() = _binding!!

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                viewModel.onScroll()
            }
        }
    }

    private val recyclerViewLayoutChangeListener =
        OnLayoutChangeListener { view, _, _, _, bottom, _, _, _, oldBottom ->
            Log.d(LOG_TAG, "RecyclerView layout changed ($bottom, $oldBottom).")

            val scrollIndex = keypadAvoidingScrollIndex ?: return@OnLayoutChangeListener

            when {
                bottom < oldBottom -> {
                    Log.d(LOG_TAG, "ContentFragment posting scroll to index $scrollIndex.")

                    view.post { scrollToIndex(scrollIndex) }
                    keypadAvoidingScrollIndex = null
                }

                bottom > oldBottom  -> keypadAvoidingScrollIndex = null

                bottom == oldBottom -> {
                    // Don't clear keypadAvoidingScrollIndex here! Sometimes several layout passes
                    // occur as preparation for animating the keypad in, without reducing the height
                    // of the RecyclerView.
                }
            }
        }

    private var keypadAvoidingScrollIndex: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentContentBinding.inflate(inflater, container, false)

        val customAdapter = ContentAdapter(tapHandler = viewModel::onItemSelected)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = customAdapter
            itemAnimator = null
            addOnScrollListener(scrollListener)
            addOnLayoutChangeListener(recyclerViewLayoutChangeListener)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect {
                customAdapter.submitList(it.data)
            }
        }

        lifecycleScope.launchWhenStarted {
            for (scrollInstruction in viewModel.scrollInstructions) {
                val scrollIndex = scrollInstruction.index

                Log.d(LOG_TAG, "ContentFragment immediately scrolling to index $scrollIndex.")
                scrollToIndex(scrollIndex)

                if (scrollInstruction.keypadWillEnter) {
                    // We don't know whether the keypad has already entered or will shortly enter.
                    // If it has not yet entered, the call to scrollToIndex above was premature and
                    // the keypad may still end up covering the row we're trying to make visible.
                    //
                    // We escape this scenario by temporarily saving the scroll index and calling
                    // scrollToIndex again later if we detect a keypad entrance. We detect an
                    // entrance by monitoring the height of our RecyclerView - if it shrinks, the
                    // keypad has entered. (We can't monitor keypad position directly since it's not
                    // part of our own view hierarchy.)
                    //
                    // We have to be pretty defensive about clearing the temporary scroll index to
                    // make sure we don't apply it if the keypad has already entered and the call to
                    // scrollToIndex above was _not_ premature.

                    Log.d(LOG_TAG, "ContentFragment setting post layout scroll index $scrollIndex.")
                    keypadAvoidingScrollIndex = scrollIndex
                } else {
                    keypadAvoidingScrollIndex = null
                }
            }
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            })

        return binding.root
    }

    override fun onDestroyView() {
        binding.recyclerView.apply {
            removeOnLayoutChangeListener(recyclerViewLayoutChangeListener)
            removeOnScrollListener(scrollListener)
        }

        _binding = null

        super.onDestroyView()
    }

    private fun scrollToIndex(index: Int) {
        binding.recyclerView.scrollToPosition(index)
    }

}
