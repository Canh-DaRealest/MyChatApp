package com.canhmai.chatapp.activity

import android.content.Context
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.canhmai.chatapp.R
import com.canhmai.chatapp.adapter.ListFriendAdapter
import com.canhmai.chatapp.base.BaseActivity
import com.canhmai.chatapp.databinding.ActivityMainBinding
import com.canhmai.chatapp.extension.auth
import com.canhmai.chatapp.extension.openActivity
import com.canhmai.chatapp.model.ListFriendMessage
import com.canhmai.chatapp.viewmodel.StartActVM
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : BaseActivity<ActivityMainBinding, StartActVM>() {

    lateinit var listFriendAdapter: ListFriendAdapter
    private lateinit var database: DatabaseReference
// ...


    override fun getCLassViewModel(): StartActVM {
        val vm: StartActVM by viewModels()
        return vm
    }

    override fun initWidgets() {
        val db = Firebase.firestore
        database = Firebase.database.reference

        binding.topAppBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {

            when (it.itemId) {
                R.id.item_sign_out -> {
                    signOut()
                    true
                }
                else -> false

            }
        })

    }

    private fun signOut() {
        auth.signOut()

        if (auth.currentUser == null) {
            this.openActivity(StartActivity::class.java)
        }
    }

    override
    fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private fun updateListFriendAdapter(context: Context, list: List<ListFriendMessage>) {

        listFriendAdapter = ListFriendAdapter(context, list)

        binding.rvMessageList.adapter = listFriendAdapter


    }
}