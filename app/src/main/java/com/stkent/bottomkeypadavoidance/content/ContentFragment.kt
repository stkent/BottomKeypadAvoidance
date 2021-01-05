package com.stkent.bottomkeypadavoidance.content

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
        }

        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect {
                customAdapter.submitList(it.data)
            }
        }

        lifecycleScope.launchWhenStarted {
            for (index in viewModel.scrollToIndex) {
                Log.d(LOG_TAG, "ContentFragment scrolling to index $index.")
                binding.recyclerView.scrollToPosition(index)
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
        _binding = null
        binding.recyclerView.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }

}
