package com.stkent.bottomkeypadavoidance.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import com.stkent.bottomkeypadavoidance.databinding.RowBinding

class ContentAdapter(
    private val tapHandler: (Int) -> Unit
) : ListAdapter<SelectableInt, ContentViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK: ItemCallback<SelectableInt> =
            object : ItemCallback<SelectableInt>() {
                override fun areItemsTheSame(
                    oldItem: SelectableInt,
                    newItem: SelectableInt
                ): Boolean {

                    return oldItem.value == newItem.value
                }

                override fun areContentsTheSame(
                    oldItem: SelectableInt,
                    newItem: SelectableInt
                ): Boolean {

                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(
            RowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val selectableInt = getItem(position)
        holder.bind(selectableInt)
        holder.itemView.rootView.setOnClickListener { tapHandler(selectableInt.value) }
    }

}
