package com.wavemoney.currencyconverter.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.wavemoney.currencyconverter.R
import com.wavemoney.currencyconverter.databinding.FragmentConvertBinding
import com.wavemoney.currencyconverter.ui.activity.HomeActivity
import com.wavemoney.currencyconverter.utility.delegateutils.fragmentViewBinding
import com.wavemoney.currencyconverter.utility.kodeinViewModel
import com.wavemoney.currencyconverter.utility.showToast
import com.wavemoney.currencyconverter.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * ConvertFragment
 * Button Click Control
 * check DB is empty or not
 * If DB is empty, observe data from API
 * If DB is not empty, add currency names into the array
 * Then set dropdowns for the currency selections.
 * Then calculate the value and show result
 */
class ConvertFragment : Fragment(R.layout.fragment_convert), DIAware {
    override val di by closestDI()
    private val binding by fragmentViewBinding(FragmentConvertBinding::bind)
    private val homeViewModel: HomeViewModel by kodeinViewModel()
    var currencyList = ArrayList<String>()
    var currencySelection1 = ""
    var currencySelection2 = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as HomeActivity).title = "Convert"
        buttonClicksControl()
        CoroutineScope(Dispatchers.Main).launch {
            checkDB()
            setDropDown()
        }
    }

    /**
     * Controlling the button clicks
     * check the value is not blank
     * if not blank, check currency selections
     * if currency selections already chose, calculate function triggered
     * currency change button triggered the switch between 2 selected currencies
     * i.e THB -> MMK will become MMK -> THB
     */
    private fun buttonClicksControl() {
        binding.btnConvert.setOnClickListener {
            if (isNotBlanks()) {
                when {
                    currencySelection1 == "" -> {
                        requireContext().showToast("Please Choose Source Currency !!!")
                    }
                    currencySelection2 == "" -> {
                        requireContext().showToast("Please Choose Target Currency !!!")
                    }
                    else -> {
                        calculate()
                    }
                }
            }
        }
        binding.imgChangeCurrency.setOnClickListener {
            when {
                currencySelection1 == "" -> {
                    requireContext().showToast("Please Choose Source Currency !!!")
                }
                currencySelection2 == "" -> {
                    requireContext().showToast("Please Choose Target Currency !!!")
                }
                else -> {
                    val selection1 = currencyList.find { it == currencySelection2 }!!
                    val selection2 = currencyList.find { it == currencySelection1 }!!
                    binding.ctvCurrency1.setText(selection1, false)
                    currencySelection1 = selection1

                    binding.ctvCurrency2.setText(selection2, false)
                    currencySelection2 = selection2
                }
            }
        }
    }

    /**
     * Check the DB is empty or not
     */
    private suspend fun checkDB() {
        val rates = homeViewModel.getLiveRatesFromDB()
        currencyList.clear()
        if (rates.isEmpty()) {
            observeList()
        } else {
            rates.forEach {
                currencyList.add(it.currency)
            }
        }
    }

    /**
     * setting the dropdown for currency selections
     * get the selected currency value and assign it into the variables.
     */
    private fun setDropDown() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.list_item_drop_down,
            currencyList
        )
        binding.ctvCurrency1.setAdapter(adapter)
        binding.ctvCurrency1.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                currencySelection1 = currencyList[position]
            }

        binding.ctvCurrency2.setAdapter(adapter)
        binding.ctvCurrency2.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                currencySelection2 = currencyList[position]
            }

    }

    /**
     * Observing the rates from API and insert all into local currency list
     */
    private fun observeList() {
        homeViewModel.getLiveRatesLiveData().observe(viewLifecycleOwner, {
            it.forEach { liveRates ->
                currencyList.add(liveRates.currency)
            }
        })
    }

    /**
     * Check that if edittext of value is not null or empty.
     */
    private fun isNotBlanks(): Boolean {
        var isNotBlanks = false
        when {
            TextUtils.isEmpty(binding.edtCurrency1.text!!.trim().toString()) -> {
                requireContext().showToast("Please Enter Input Value !!!")
            }
            else -> {
                isNotBlanks = true
            }
        }
        return isNotBlanks
    }

    /**
     * Calculate the result of conversion.
     * 1. get the 1st selection rates from USD. i.e 1 USD to 1st selection
     * 2. get the 2nd selection rates from USD. i.e 1 USD to 2nd selection
     * 3. Then, make deciaml point only visible to 6 place.
     * 4. Check that, 1st selection is whethere USD or not.
     * 5. If 1st selection is USD, show the result directly.
     * 6. If 1st selection is not USD, calculate the result.
     * 7. Divide 2nd selection rate with 1st selection rate.
     */
    private fun calculate() {
        Log.d("HIHIHI", "$currencySelection1")
        Log.d("HIHIHI", "$currencySelection2")

        CoroutineScope(Dispatchers.Main).launch {
            val rate1: Double =
                homeViewModel.getRateByName(currencySelection1)!!.rates
            val rate2: Double =
                homeViewModel.getRateByName(currencySelection2)!!.rates
            val df = DecimalFormat("#.######")
            df.roundingMode = RoundingMode.CEILING
            when (currencySelection1) {
                "USD" -> {
                    binding.txtCurrency2.text = rate2.toString()
                }
                else -> {
                    //sample calculation ------------------------------------------------------
                    //33.409884 USD to THB
                    //1773.121226 USD TO MMK
                    //53.20            THB to MMK
                    //0.0188424139     MMK to THB
                    binding.txtCurrency2.text = "${df.format(BigDecimal(rate2 / rate1))}"
                }
            }
        }
    }
}
