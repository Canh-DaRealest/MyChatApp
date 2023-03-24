package com.canhmai.chatapp.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
    clz: Class<*>, bundle: Bundle? = null, isNoHistory: Boolean = true
) {
    val intent = Intent(ctx, clz)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    if (isNoHistory) {
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    startActivity(intent)
}

 fun AppCompatActivity.updateUserStatus(status: String) {
    val reference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)

    val hashMap: HashMap<String, Any> = HashMap()

    hashMap.put("status", status)

    reference.updateChildren((hashMap))

}


