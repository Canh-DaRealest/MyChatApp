package com.canhmai.chatapp.extension

import android.content.Context
import android.view.View
import android.widget.TextView
import com.canhmai.chatapp.R
import com.google.android.material.snackbar.Snackbar


inline val Context.ctx: Context
    get() = this


fun Context.showSnackBar(view: View, msg: String, isShowLong: Boolean) {
    val duration: Int = if (isShowLong) {
        Snackbar.LENGTH_LONG
    } else {
        Snackbar.LENGTH_SHORT
    }
    val snackbar = Snackbar.make(view, msg, duration)

    snackbar.setBackgroundTint(this.resources.getColor(R.color.snackbar_color))
    val v = snackbar.view
    val textview = v.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textview.setTextColor(this.resources.getColor(R.color.white))
    snackbar.show()

}

