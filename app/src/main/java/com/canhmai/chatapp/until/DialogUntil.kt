package com.canhmai.chatapp.until

import android.app.Dialog
import android.content.Context
import com.canhmai.chatapp.R

object DialogUntil {
    private  var dialog: Dialog? = null

    private fun createProgressDialog(activity: Context): Dialog {

        var dialog = Dialog(activity)
        dialog.setContentView(
            R.layout.layout_progress
        )

        return dialog
    }

    fun showDialog(activity: Context) {
        if (dialog != null && !dialog!!.isShowing) {
            dialog!!.show()
        } else if (dialog == null) {
            dialog = createProgressDialog(activity)
            dialog!!.show()
        }

    }

    fun dismissDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }


}
