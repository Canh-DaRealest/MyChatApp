package com.canhmai.chatapp.activity

import android.os.Handler
import androidx.activity.viewModels
import com.canhmai.chatapp.adapter.ViewPager
import com.canhmai.chatapp.base.BaseActivity
import com.canhmai.chatapp.databinding.AcitvitySpashBinding
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

class SplashActivity : BaseActivity<AcitvitySpashBinding, StartActVM>() {
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

    override fun initWidgets() {

    }

    override fun onStart() {
        super.onStart()
        val handler: Handler = Handler()
        handler.postDelayed(Runnable {
            if (auth.currentUser != null) {
                this.openActivity(MainActivity::class.java)

            } else {
                this.openActivity(StartActivity::class.java)
            }

        }, 2000)
    }


    override fun getViewBinding(): AcitvitySpashBinding {
        return AcitvitySpashBinding.inflate(layoutInflater)
    }


}
