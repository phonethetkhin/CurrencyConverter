package com.wavemoney.currencyconverter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wavemoney.currencyconverter.databinding.ListItemAvailableListBinding
import com.wavemoney.currencyconverter.roomdb.entity.AvailableEntity
import com.wavemoney.currencyconverter.utility.delegateutils.adapterViewBinding

class AvailableListAdapter :
    ListAdapter<AvailableEntity, AvailableListAdapter.AvailableViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<AvailableEntity>() {
            override fun areItemsTheSame(
                oldItem: AvailableEntity,
                newItem: AvailableEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AvailableEntity,
                newItem: AvailableEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class AvailableViewHolder(val binding: ListItemAvailableListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableViewHolder {
        val v = parent.adapterViewBinding(ListItemAvailableListBinding::inflate)
        return AvailableViewHolder(v)
    }

    override fun onBindViewHolder(holder: AvailableViewHolder, position: Int) {
        val available = getItem(position)
        setData(available, holder)
    }

    private fun setData(available: AvailableEntity, holder: AvailableViewHolder) {
        with(holder)
        {
            binding.txtAvailable.text = available.available
        }
    }
}