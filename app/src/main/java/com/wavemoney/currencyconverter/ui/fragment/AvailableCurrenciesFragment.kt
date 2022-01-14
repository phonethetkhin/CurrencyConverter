package com.wavemoney.currencyconverter.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.wavemoney.currencyconverter.R
import com.wavemoney.currencyconverter.adapter.AvailableListAdapter
import com.wavemoney.currencyconverter.databinding.FragmentAvailableCurrenciesBinding
import com.wavemoney.currencyconverter.ui.activity.HomeActivity
import com.wavemoney.currencyconverter.utility.delegateutils.fragmentViewBinding
import com.wavemoney.currencyconverter.utility.getBooleanPref
import com.wavemoney.currencyconverter.utility.kodeinViewModel
import com.wavemoney.currencyconverter.utility.setBooleanPref
import com.wavemoney.currencyconverter.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI

/**
 * AvailableCurrenciesFragment(Parent Fragment)
 * check DB is empty or not
 * If DB is empty, observe data from API
 * If DB is not empty, check refresh is true or not
 * If true, observe data from API
 */
class AvailableCurrenciesFragment : Fragment(R.layout.fragment_available_currencies), DIAware {
    override val di by closestDI()
    private val binding by fragmentViewBinding(FragmentAvailableCurrenciesBinding::bind)
    private val homeViewModel: HomeViewModel by kodeinViewModel()
    private lateinit var availableAdapter: AvailableListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as HomeActivity).title = "Available Currencies"
        availableAdapter = AvailableListAdapter()
        setAdapter()
        CoroutineScope(Dispatchers.Main).launch {
            checkDB()
        }
    }

    /**
     *Checking DB is empty or not
     * if empty, observe data from API
     *if not empty, check refresh, if refresh, get data from API.
     * else get from DB
     */
    private suspend fun checkDB() {
        val availableList = homeViewModel.getAvailableFromDB()
        if (availableList.isEmpty()) {
            observeList()
        } else {
            val refresh = getBooleanPref(requireContext(), "Timer", "Timer", false)
            if (refresh) {
                observeList()
            } else {
                availableAdapter.submitList(availableList)
            }
        }
    }

    /**
     * Setting the adapter
     * Added Simple Divider Line
     */
    private fun setAdapter() {
        binding.rcvAvailableList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        binding.rcvAvailableList.adapter = availableAdapter
    }

    /**
     * observeData from the API
     * after observing data from the API, make refresh to false again
     * after that 30 minutes, refresh will triggered again and refresh agaain
     */
    private fun observeList() {
        homeViewModel.getAvailableLiveData().observe(viewLifecycleOwner, {
            availableAdapter.submitList(it)
            setBooleanPref(requireContext(), "Timer", "Timer", false)
        })
    }

}