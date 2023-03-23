package com.canhmai.chatapp.extension

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import com.canhmai.chatapp.R

fun EditText.handlePasswordVisibility(toggle: ImageView?) {
    val curPos = this.selectionStart
    if (this.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
        toggle?.setImageResource(R.drawable.ic_password_on)

        this.transformationMethod =
            PasswordTransformationMethod.getInstance()


    } else {
        toggle?.setImageResource(R.drawable.ic_password_off)

        this.transformationMethod =
            HideReturnsTransformationMethod.getInstance()

    }
    this.setSelection(curPos)
}