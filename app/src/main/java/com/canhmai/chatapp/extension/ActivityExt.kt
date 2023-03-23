package com.canhmai.chatapp.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.replaceFragment(containerView: Int, fragment: Fragment, isBacked: Boolean) {
    val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(containerView, fragment)
    if (isBacked) fragmentTransaction.addToBackStack(fragment.tag)

    fragmentTransaction.commit()
}

fun AppCompatActivity.addFragment(containerView: Int, fragment: Fragment, isBacked: Boolean) {
    val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.add(containerView, fragment)
    if (isBacked) fragmentTransaction.addToBackStack(fragment.arguments?.getString("TAG"))

    fragmentTransaction.commit()
}

fun AppCompatActivity.openActivity(
    clz: Class<*>, bundle: Bundle? = null, clearStack: Boolean = false,
    enterAnim: Int? = null, exitAnim: Int? = null, flags: IntArray? = null
) {
    val intent = Intent(ctx, clz)
    if (flags?.isNotEmpty() == true) {
        for (flag in flags) {
            intent.addFlags(flag)
        }
    }
    if (clearStack) {
        setResult(Activity.RESULT_CANCELED)
        finishAffinity()
    }
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
    enterAnim?.also { enter ->
        exitAnim?.also { exit ->
            overridePendingTransition(enter, exit)
        }
    }
}

fun AppCompatActivity.openActivity(
    clz: Class<*>, isNoHistory: Boolean = true
) {
    val intent = Intent(ctx, clz)

    if (isNoHistory) {
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    startActivity(intent)
}
