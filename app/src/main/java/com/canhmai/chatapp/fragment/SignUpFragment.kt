package com.canhmai.chatapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.canhmai.chatapp.activity.MainActivity
import com.canhmai.chatapp.base.BaseFragment
import com.canhmai.chatapp.databinding.FragmentSignupBinding
import com.canhmai.chatapp.extension.auth
import com.canhmai.chatapp.extension.openActivity
import com.canhmai.chatapp.extension.showSnackBar
import com.canhmai.chatapp.model.User

import com.canhmai.chatapp.until.CommonUntil
import com.canhmai.chatapp.viewmodel.StartActVM

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : BaseFragment<FragmentSignupBinding, StartActVM>() {
    lateinit var reference: DatabaseReference


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


        binding.btRegister.setOnClickListener {
            val email = binding.edtSignupEmail.text?.trim().toString()
            val password = binding.edtSignupPassword.text?.trim().toString()
            val name = binding.edtSignupName.text?.trim().toString()
            val password2 = binding.edtSignupConfirmPassword.text?.trim().toString()

            signUpWithEmailPassword(name, email, password, password2)

        }
    }


    private fun signUpWithEmailPassword(
        name: String,
        email: String,
        password: String,
        password2: String
    ) {

        var msg: String

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || password2.isEmpty()) {
            msg = "Bạn chưa điền đầy đủ thông tin"
            requireContext().showSnackBar(binding.lnRegisterContainer, msg, false)
            return
        }
        if (password2 != password) {
            msg = "Mật khẩu không khớp"
            requireContext().showSnackBar(binding.lnRegisterContainer, msg, false)
            return
        }

        CommonUntil.closeKeyboard(parentActivity)
        hideView(true)
        // [START sign_in_with_email]

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(parentActivity) { task ->
                if (task.isSuccessful) {

                    val user: FirebaseUser? = auth.currentUser
                    val userId: String = user!!.uid
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap.put("userId", userId)
                    hashMap.put("userName", name)
                    hashMap.put("userImage", "default")
                    hashMap.put("email", user.email!!)
                    hashMap.put("lastMsg", "")
                    hashMap.put("lastTime", "")
                    hashMap.put("status", "online")

                    reference.setValue(hashMap)
                        .addOnCompleteListener(requireActivity()) { task ->

                            if (task.isSuccessful) {

                                parentActivity.openActivity(MainActivity::class.java)
                                Toast.makeText(
                                    requireContext(),
                                    "Đăng ký thành công ",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                Log.e(TAG, "error update user: ", task.exception)
                            }

                        }

                } else {

                    msg = CommonUntil.catchFireBaseException(task)

                    showErrorSnackBar("Đăng ký thất bại, $msg")
                    Log.e(TAG, "signInWithEmailPassword: ", task.exception)
                }
                hideView(false)
            }
    }


    private fun hideView(state: Boolean) {
        if (state) {
            binding.btRegister.visibility = View.GONE
            binding.lnProgress.visibility = View.VISIBLE
        } else {
            binding.btRegister.visibility = View.VISIBLE
            binding.lnProgress.visibility = View.GONE
        }
    }

}
