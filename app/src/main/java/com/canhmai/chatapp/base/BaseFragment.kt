package com.canhmai.chatapp.base

import android.content.Context


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.canhmai.chatapp.`interface`.OnShowSnackbar

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    /* activity context */
    protected lateinit var parentActivity: AppCompatActivity
    lateinit var onShowSnackbar: OnShowSnackbar
    protected lateinit var viewModel: VM
    private var _binding: VB? = null
    val binding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onShowSnackbar = context as OnShowSnackbar
            parentActivity = context as AppCompatActivity
        } catch (e: ClassCastException) {
            throw java.lang.ClassCastException("$context ${e.message}, ${e.localizedMessage}")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = getClassViewModel()
        _binding = getViewBinding()

        if (_binding == null)
            throw java.lang.IllegalArgumentException("_binding must not be null")
        return binding.root
    }

    abstract fun getClassViewModel(): VM

    abstract fun getViewBinding(): VB


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    abstract fun initViews(view: View)
    override fun onDestroy() {
        _binding = null
        super.onDestroy()


    }


    protected open fun clickView(v: View?) {

    }

    open fun addFragment(containerView: Int, fragment: Fragment) {

        childFragmentManager.beginTransaction().add(containerView, fragment)
            .commit()


    }

    open fun showErrorSnackBar(msg: String) {
        onShowSnackbar.onShowSnackbar(msg)
    }

}