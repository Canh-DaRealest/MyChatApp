package com.canhmai.chatapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.canhmai.chatapp.`interface`.OnShowSnackbar

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity(),
    OnShowSnackbar {
    protected lateinit var viewModel: VM

    protected lateinit var binding: VB

    /*context*/
    protected val self by lazy { this }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        viewModel = getCLassViewModel()

        setContentView(binding.root)

        initWidgets()
    }

    abstract fun getCLassViewModel(): VM

    abstract fun initWidgets()


    abstract fun getViewBinding(): VB


    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onRestart() {
        super.onRestart()
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onShowSnackbar(msg: String) {
        showError(msg)
    }

    open fun showError(msg: String) {

    }


}
