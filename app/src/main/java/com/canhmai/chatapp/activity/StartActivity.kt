package com.canhmai.chatapp.activity

import androidx.activity.viewModels
import com.canhmai.chatapp.adapter.ViewPager
import com.canhmai.chatapp.base.BaseActivity
import com.canhmai.chatapp.databinding.ActivityStartBinding
import com.canhmai.chatapp.extension.auth
import com.canhmai.chatapp.extension.openActivity
import com.canhmai.chatapp.extension.showSnackBar
import com.canhmai.chatapp.viewmodel.StartActVM
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.AuthCredential
import com.google.firebase.database.DatabaseReference

class StartActivity : BaseActivity<ActivityStartBinding, StartActVM>() {
    lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var credential: AuthCredential
    lateinit var reference: DatabaseReference

    private var isLogin = true

    private lateinit var viewPager: ViewPager
    override fun getCLassViewModel(): StartActVM {
        val vm: StartActVM by viewModels()
        return vm
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            this.openActivity(MainActivity::class.java)
            isLogin = true
        } else {
            isLogin = false
        }
    }

    override fun initWidgets() {

        if (!isLogin) {
            initViewPager()
        }


    }


    private fun initViewPager() {

        viewPager = ViewPager(this)
        binding.vp2StartViewpager2.adapter = viewPager

        TabLayoutMediator(binding.tlStartTablayout, binding.vp2StartViewpager2) { tab, postition ->
            tab.text = viewPager.listTitle[postition]

        }.attach()

    }


    override fun getViewBinding(): ActivityStartBinding {
        return ActivityStartBinding.inflate(layoutInflater)
    }


    override fun showError(msg: String) {
        super.showError(msg)
        this.showSnackBar(binding.lnStartMain, msg)
    }
}
