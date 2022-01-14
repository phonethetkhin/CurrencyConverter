package com.wavemoney.currencyconverter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wavemoney.currencyconverter.databinding.ListItemLiveRatesBinding
import com.wavemoney.currencyconverter.roomdb.entity.LiveRatesEntity
import com.wavemoney.currencyconverter.utility.delegateutils.adapterViewBinding

class LiveRatesAdapter :
    ListAdapter<LiveRatesEntity, LiveRatesAdapter.LiveRatesViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<LiveRatesEntity>() {
            override fun areItemsTheSame(
                oldItem: LiveRatesEntity,
                newItem: LiveRatesEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: LiveRatesEntity,
                newItem: LiveRatesEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class LiveRatesViewHolder(val binding: ListItemLiveRatesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveRatesViewHolder {
        val v = parent.adapterViewBinding(ListItemLiveRatesBinding::inflate)
        return LiveRatesViewHolder(v)
    }

    override fun onBindViewHolder(holder: LiveRatesViewHolder, position: Int) {
        val liveRates = getItem(position)
        setData(liveRates, holder)
    }

    private fun setData(liveRates: LiveRatesEntity, holder: LiveRatesViewHolder) {
        with(holder)
        {
            binding.txtCurrencyTitle.text = liveRates.currency      // returns last 3
            binding.txtCurrency.text = liveRates.rates.toString()
        }
    }
}