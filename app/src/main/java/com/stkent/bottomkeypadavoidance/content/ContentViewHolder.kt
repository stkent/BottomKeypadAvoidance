package com.stkent.bottomkeypadavoidance.content

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stkent.bottomkeypadavoidance.R
import com.stkent.bottomkeypadavoidance.databinding.RowBinding

class ContentViewHolder(private val binding: RowBinding) : ViewHolder(binding.root) {

    fun bind(selectableInt: SelectableInt) {
        itemView.isSelected = selectableInt.selected

        binding.label.text = "Item ${selectableInt.value}"

        binding.background.setBackgroundResource(
            if (selectableInt.selected) R.color.purple_200 else R.color.gray
        )
    }

}
