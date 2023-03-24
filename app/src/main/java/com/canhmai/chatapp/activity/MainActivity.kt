package com.canhmai.chatapp.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.canhmai.chatapp.adapter.ListUsersAdapter
import com.canhmai.chatapp.base.BaseActivity
import com.canhmai.chatapp.databinding.ActivityMainBinding
import com.canhmai.chatapp.extension.*
import com.canhmai.chatapp.model.User
import com.canhmai.chatapp.viewmodel.StartActVM
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity : BaseActivity<ActivityMainBinding, StartActVM>() {

    private lateinit var listUserAdapter: ListUsersAdapter

    private lateinit var listUser: MutableList<User>
    private lateinit var firebaseUser: FirebaseUser

    override fun getCLassViewModel(): StartActVM {
        val vm: StartActVM by viewModels()
        return vm
    }

    override fun initWidgets() {
        firebaseUser = auth.currentUser!!
        getListUser()
        listUser = mutableListOf()
        binding.ciUserImage.setOnClickListener(View.OnClickListener { openDrawer() })
        binding.btSignOut.setOnClickListener(View.OnClickListener { signOut() })

    }

    private fun getListUser() {

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listUser.clear()

                try {
                    for (snapshot: DataSnapshot in dataSnapshot.children) {


                        val user = snapshot.getValue(User::class.java)

                        if (user!!.userId != firebaseUser.uid) {

                            listUser.add(user)
                        }
                    }
                    listUserAdapter = ListUsersAdapter(ctx, listUser)
                    binding.rvMessageList.adapter = listUserAdapter
                    binding.rvMessageList.layoutManager = (LinearLayoutManager(ctx))

                    listUserAdapter.userLiveData.observe(this@MainActivity, Observer {

                        val bundle: Bundle = Bundle()
                        bundle.putString("users_id", it.userId)
                        bundle.putString("users_img", it.userImage)
                        this@MainActivity.openActivity(ChatActivity::class.java, bundle, false)

                    })

                } catch (e: DatabaseException) {
                    Log.e(TAG, "onDataChange: ${e.message}")
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }


    private fun openDrawer() {
        binding.drawerlayout.open()
    }

    private fun closeDrawer() {
        binding.drawerlayout.close()
    }

    private fun signOut() {
        closeDrawer()

        auth.signOut()

        if (auth.currentUser == null) {
            this.openActivity(StartActivity::class.java)
        }
    }

    override
    fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }


    override fun showError(msg: String) {
        super.showError(msg)
        this.showSnackBar(binding.lnLinearInside, msg)
    }



    override fun onPause() {
        super.onPause()
        this.updateUserStatus("offline")
    }

    override fun onResume() {
        super.onResume()
        this.updateUserStatus("online")
    }

}