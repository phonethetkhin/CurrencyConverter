package com.wavemoney.currencyconverter.utility

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

inline fun <reified VM : ViewModel, T> T.kodeinViewModel(): Lazy<VM> where T : DIAware, T : AppCompatActivity {
    return lazy { ViewModelProvider(this, direct.instance()).get(VM::class.java) }
}

inline fun <reified VM : ViewModel, T> T.kodeinViewModel(): Lazy<VM> where T : DIAware, T : Fragment {
    return lazy { ViewModelProvider(requireActivity(), direct.instance()).get(VM::class.java) }
}