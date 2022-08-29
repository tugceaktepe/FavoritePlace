package com.aktepetugce.favoriteplace.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar


open class BaseFragment<T : ViewBinding>(
    private val inflateMethod : (LayoutInflater, ViewGroup?, Boolean) -> T,
    private val hasOptionsMenu : Boolean) : Fragment(){

    private var _binding : T? = null
    val binding : T get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateMethod.invoke(inflater, container, false)
        if(hasOptionsMenu) {
            setHasOptionsMenu(true)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun hideKeyboard(){
        val view: View? = activity?.currentFocus
        if (view != null) {
            val inputMethodManager: InputMethodManager? =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showErrorMessage(errorMessage : String){
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }
}