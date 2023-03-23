package com.canhmai.chatapp.until

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.canhmai.chatapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException


object CommonUntil {



    fun closeKeyboard(activity: AppCompatActivity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        activity.currentFocus?.clearFocus()
    }

    fun catchFireBaseException(task: Task<AuthResult>): String {
        val msg: String
        try {
            throw task.exception!!
        } catch (e: FirebaseAuthWeakPasswordException) {


            msg = "Mật khẩu yếu, vui lòng thử lại"


        } catch (e: FirebaseAuthInvalidCredentialsException) {

            msg =
                "email hoặc mật khẩu không hợp lệ, vui lòng kiểm tra lại"

        } catch (e: FirebaseAuthUserCollisionException) {
            msg =
                "Người dùng đã tồn tại, vui lòng thử lại"

        } catch (e: FirebaseNetworkException) {
            msg =
                "Kết nối không ổn định, vui lòng thử lại"

        } catch (e: Exception) {

            msg =
                e.message!!


        }
        return msg
    }

}

