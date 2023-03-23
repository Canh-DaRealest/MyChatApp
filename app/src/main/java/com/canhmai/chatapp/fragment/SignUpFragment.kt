package com.canhmai.chatapp.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.canhmai.chatapp.base.BaseFragment
import com.canhmai.chatapp.databinding.FragmentSignupBinding
import com.canhmai.chatapp.extension.auth
import com.canhmai.chatapp.extension.handlePasswordVisibility
import com.canhmai.chatapp.extension.showSnackBar
import com.canhmai.chatapp.until.CommonUntil
import com.canhmai.chatapp.until.DialogUntil
import com.canhmai.chatapp.viewmodel.StartActVM
import com.google.firebase.auth.FirebaseUser

class SignUpFragment : BaseFragment<FragmentSignupBinding, StartActVM>() {


    companion object {
        private val TAG: String = SignUpFragment::javaClass.name

        fun newInstance(): SignUpFragment {
            val args = Bundle()

            val fragment = SignUpFragment()
            args.putString("TAG", TAG)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getClassViewModel(): StartActVM {

        val vm: StartActVM by activityViewModels()
        return vm
    }

    override fun getViewBinding(): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun initViews(view: View) {

        initClickListener()
    }


    private fun initClickListener() {


        binding.ivSignupShowPass.setOnClickListener {
            binding.edtSignupPassword.handlePasswordVisibility(
                binding.ivSignupShowPass
            )
        }
        binding.btRegister.setOnClickListener {
            val email = binding.edtSignupEmail.text.toString()
            val password = binding.edtSignupPassword.text.toString()

            signInWithEmailPassword(email, password)


        }
    }


    private fun signInWithEmailPassword(email: String, password: String) {


        var msg: String

        if (email.isEmpty() || password.isEmpty()) {
            msg = "Email hoặc mật khẩu không được để trống!"
            requireContext().showSnackBar(binding.lnRegisterContainer, msg, false)
            return
        }
        DialogUntil.showDialog(
            parentActivity
        )
        CommonUntil.closeKeyboard(parentActivity)

        // [START sign_in_with_email]

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(parentActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(requireContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
                    msg = CommonUntil.catchFireBaseException(task)
                    //     parentActivity.showSnackBar(binding.lnLoginFrgContainer, msg, false)

                }
                DialogUntil.dismissDialog(
                )
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(parentActivity, "đăng nhập thành công", Toast.LENGTH_SHORT).show()
            Log.i(
                ContentValues.TAG,
                "sign in success: ${user.email}, ${user.displayName}, ${user.getIdToken(false)}"
            )
            viewModel.updateLiveData(user)
        } else {
            Toast.makeText(parentActivity, "đăng nhập thất bại", Toast.LENGTH_SHORT).show()
        }

    }


}
