package com.canhmai.chatapp.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.canhmai.chatapp.activity.MainActivity
import com.canhmai.chatapp.base.BaseFragment
import com.canhmai.chatapp.databinding.FragmentLoginBinding
import com.canhmai.chatapp.extension.auth
import com.canhmai.chatapp.extension.openActivity
import com.canhmai.chatapp.extension.showSnackBar
import com.canhmai.chatapp.until.CommonUntil
import com.canhmai.chatapp.viewmodel.StartActVM
import com.google.firebase.auth.FirebaseUser

class LoginFragment : BaseFragment<FragmentLoginBinding, StartActVM>() {


    companion object {
        private val TAG: String = LoginFragment::javaClass.name

        fun newInstance(): LoginFragment {
            val args = Bundle()

            val fragment = LoginFragment()
            args.putString("TAG", TAG)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getClassViewModel(): StartActVM {

        val vm: StartActVM by activityViewModels()
        return vm
    }

    override fun getViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun initViews(view: View) {

        initClickListener()
    }


    private fun initClickListener() {


        binding.btLogin.setOnClickListener {
            val email = binding.edtLoginEmail.text?.trim().toString()
            val password = binding.edtLoginPassword.text?.trim().toString()

            signInWithEmailPassword(email, password)


        }
    }

    private fun signInWithEmailPassword(email: String, password: String) {

        hideView(true)
        var msg: String

        if (email.isEmpty() || password.isEmpty()) {
            msg = "Email hoặc mật khẩu không được để trống!"
            requireContext().showSnackBar(binding.lnLoginFrgContainer, msg, false)
            return
        }


        // [START sign_in_with_email]
        CommonUntil.closeKeyboard(parentActivity)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(parentActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.

                    msg = CommonUntil.catchFireBaseException(task)

                    showErrorSnackBar(msg)
                    Log.e(TAG, "signInWithEmailPassword: ", task.exception)

                }

                hideView(false)
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(parentActivity, "đăng nhập thành công", Toast.LENGTH_SHORT).show()
            Log.i(
                ContentValues.TAG,
                "sign in success: ${user.email}, ${user.displayName}, ${user.uid}"
            )
            parentActivity.openActivity(MainActivity::class.java)
        } else {
            Toast.makeText(parentActivity, "đăng nhập thất bại", Toast.LENGTH_SHORT).show()
        }

    }

    private fun hideView(state: Boolean) {
        if (state) {
            binding.btLogin.visibility = View.GONE
            binding.lnProgress.visibility = View.VISIBLE
        } else {
            binding.btLogin.visibility = View.VISIBLE
            binding.lnProgress.visibility = View.GONE
        }
    }

}
