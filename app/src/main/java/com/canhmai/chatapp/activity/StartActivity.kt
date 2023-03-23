package com.canhmai.chatapp.activity

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.canhmai.chatapp.R
import com.canhmai.chatapp.R.string
import com.canhmai.chatapp.adapter.ViewPager
import com.canhmai.chatapp.base.BaseActivity
import com.canhmai.chatapp.databinding.ActivityStartBinding
import com.canhmai.chatapp.extension.auth
import com.canhmai.chatapp.extension.ctx
import com.canhmai.chatapp.extension.openActivity
import com.canhmai.chatapp.extension.showSnackBar
import com.canhmai.chatapp.until.DialogUntil
import com.canhmai.chatapp.viewmodel.StartActVM
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class StartActivity : BaseActivity<ActivityStartBinding, StartActVM>() {
    lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var credential: AuthCredential

    val TAG = "FirstFragment"
    val RC_SIGN_IN = 123

    private lateinit var viewPager: ViewPager
    override fun getCLassViewModel(): StartActVM {
        val vm: StartActVM by viewModels()
        return vm
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            this.openActivity(MainActivity::class.java)
        }
    }

    override fun initWidgets() {

        viewModel.getLiveData()?.observe(this) { data ->
            if (data != null) {

                this.openActivity(MainActivity::class.java)
            }
        }
        checkIfUserLogined()

    }

    private fun checkIfUserLogined() {

        initViewPager()
        initClickListener()


    }


    private fun initViewPager() {

        viewPager = ViewPager(this)
        binding.vp2StartViewpager2.adapter = viewPager

        TabLayoutMediator(binding.tlStartTablayout, binding.vp2StartViewpager2) { tab, postition ->
            tab.text = viewPager.listTitle[postition]

        }.attach()
        getKeyHash()

        ConfigureGoogleSignIn()

    }


    override fun getViewBinding(): ActivityStartBinding {
        return ActivityStartBinding.inflate(layoutInflater)
    }


    private fun initClickListener() {


        binding.ivLoginViaGoogle.setOnClickListener {
            signInWithGoogle()
        }


    }


    private fun signInWithCredential(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth.currentUser
                    Log.d(
                        TAG,
                        "signInWithCredential:success : ${user?.displayName}, ${user?.email}, ${user?.photoUrl}, ${user?.metadata}"
                    )
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this, "Đăng nhập thất bại.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "onActivityResult: ")
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                this.showSnackBar(
                    binding.lnStartMain,
                    "Đăng nhập bằngbằnggle thất bại",
                    false
                )
            }
        }
    }

    private fun signInWithGoogle() {
        DialogUntil.showDialog(ctx)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        credential = GoogleAuthProvider.getCredential(idToken, null)
        signInWithCredential(credential)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "đăng nhập thành công", Toast.LENGTH_SHORT).show()
            viewModel.updateLiveData(user)
        } else {
            Toast.makeText(this, "đăng nhập thất bại", Toast.LENGTH_SHORT).show()
            ctx.showSnackBar(binding.lnStartMain, "Đăng nhập thất bại", false)
        }
    }

    private fun ConfigureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()
    }

    private fun getKeyHash() {
        //get keyhash
        try {
            val info: PackageInfo = this.packageManager.getPackageInfo(
                "com.canhmai.symphony",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }

    }
}
