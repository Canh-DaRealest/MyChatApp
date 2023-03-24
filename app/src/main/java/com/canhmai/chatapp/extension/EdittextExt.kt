package com.canhmai.chatapp.extension

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import com.canhmai.chatapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.handlePasswordVisibility(toggle: ImageView?) {
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

fun EditText.afterTextChanged(image: ImageView, imageBeforeChange: Int, imageAfterChange: Int) {
    this.doAfterTextChanged {
        if (!this.text.toString().isEmpty()) {
            image.setImageResource(imageAfterChange)
        } else {
            image.setImageResource(imageAfterChange)
        }
    }

}