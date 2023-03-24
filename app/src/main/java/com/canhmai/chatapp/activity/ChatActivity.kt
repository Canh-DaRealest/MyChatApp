package com.canhmai.chatapp.activity

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.canhmai.chatapp.R
import com.canhmai.chatapp.adapter.ChatAdapter
import com.canhmai.chatapp.base.BaseActivity
import com.canhmai.chatapp.databinding.ActivityChatBinding
import com.canhmai.chatapp.extension.auth
import com.canhmai.chatapp.extension.ctx
import com.canhmai.chatapp.extension.updateUserStatus
import com.canhmai.chatapp.model.Chat
import com.canhmai.chatapp.model.User
import com.canhmai.chatapp.viewmodel.StartActVM
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : BaseActivity<ActivityChatBinding, StartActVM>() {

    private val PICK_IMAGE_REQUEST = 1
    private val IMAGE_PATH = "*#%\$||{}"
    private lateinit var storageRef: StorageReference
    private lateinit var filePath: Uri
    private var selectedImageUrl: String? = null
    lateinit var firebaseUser: FirebaseUser
    lateinit var reference: DatabaseReference
    private var chatList: MutableList<Chat> = mutableListOf()
    private var userImg: String? = null
    private var userId: String? = null
    private lateinit var chatAdapter: ChatAdapter


    override fun getCLassViewModel(): StartActVM {
        val vm: StartActVM by viewModels()
        return vm
    }

    override fun initWidgets() {


        val intent: Intent = intent
        val bundle: Bundle? = intent.extras
        userId = bundle?.getString("users_id")
        userImg = bundle?.getString("users_img")

        firebaseUser = auth.currentUser!!

        storageRef = FirebaseStorage.getInstance().reference
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        receiveMsg(firebaseUser.uid, userId!!)
        updateUserUI()


        setClickView()

    }

    private fun createChatHistory(firebaseUser: FirebaseUser, userId: String) {

    }

    private fun setClickView() {
        binding.ivSentImage.setOnClickListener(View.OnClickListener { selectImage() })
        binding.ivBack.setOnClickListener(View.OnClickListener { onBackPressed() })
        binding.btSend.setOnClickListener(View.OnClickListener {
            val msg: String = binding.edtChat.text.toString()
            if (!msg.isEmpty()) {
                sendMsg(firebaseUser.uid, userId!!, msg)
                binding.edtChat.setText("")
            }
        })
        binding.ivClear.setOnClickListener(View.OnClickListener {
            if (binding.lnLayoutSendImage.visibility == View.VISIBLE) {
                binding.lnLayoutSendImage.visibility = View.GONE
            }
        })
        binding.tvSendImage.setOnClickListener(View.OnClickListener {
            if (binding.ivImageContaainer.drawable != null) {
                uploadImage()
                binding.lnLayoutSendImage.visibility = View.GONE
            }

        })
    }

    private fun updateUserUI() {
        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                binding.tvFriendName.text = user!!.userName

                if (user.userImage != "default") {
                    Glide.with(ctx).load(user.userImage).into(binding.ciUserImage)
                } else {
                    binding.ciUserImage.setImageResource(R.drawable.ic_user)
                }
                if (user.status.equals("online")) {
                    binding.vIsOnline.visibility = View.VISIBLE
                    binding.tvOnLineState.visibility = View.VISIBLE
                    binding.tvOnLineState.setText("Đang hoạt động")
                } else {
                    binding.vIsOnline.visibility = View.INVISIBLE
                    binding.tvOnLineState.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }


    override fun getViewBinding(): ActivityChatBinding {
        return ActivityChatBinding.inflate(layoutInflater)
    }


    private fun sendMsg(senderId: String, receiverId: String, msg: String) {
        reference = FirebaseDatabase.getInstance().reference
        val currentTimeInMillis = Date().time

        val dateFormat = SimpleDateFormat("hh:mm dd/MM")
        val date = dateFormat.format(currentTimeInMillis)


        val hashMap: HashMap<String, String> = HashMap()

        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("msg", msg)
        hashMap.put("time", date)
        reference.child("Chat").push().setValue((hashMap))

    }

    private fun receiveMsg(senderId: String, receiverId: String) {

        val reference = FirebaseDatabase.getInstance().getReference("Chat")

        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                chatList.clear()
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)

                    Log.i(TAG, "onDataChange: $chat")

                    if (chat?.senderId.equals(senderId) && chat?.receiverId.equals(receiverId)
                        ||
                        chat?.receiverId.equals(senderId) && chat?.senderId.equals(receiverId)
                    ) {

                        chatList.add(chat!!)
                    }
                }



                chatAdapter = ChatAdapter(ctx, chatList, userImg!!)
                binding.rvChatMsg.adapter = chatAdapter
                binding.rvChatMsg.layoutManager = LinearLayoutManager(ctx)
                binding.rvChatMsg.scrollToPosition(chatList.size - 1)
                chatAdapter.notifyDataSetChanged();


                val item = dataSnapshot.children.last().getValue(Chat::class.java)
                updateLastMsg(item!!.msg, item.receiverId, item.senderId, item.time)


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun updateLastMsg(lastMsg: String, receiverId: String, senderId: String, date: String) {
        val senderReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(senderId)
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap.put("lastMsg", lastMsg)
        hashMap.put("lastTime", date)

        senderReference.updateChildren((hashMap))


        val receiverReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(receiverId)
        val receiverHashmap: HashMap<String, Any> = HashMap()
        receiverHashmap.put("lastMsg", lastMsg)
        receiverHashmap.put("lastTime", date)

        receiverReference.updateChildren((receiverHashmap))

    }


    override fun onPause() {
        super.onPause()
        this.updateUserStatus("offline")
    }

    override fun onResume() {
        super.onResume()
        this.updateUserStatus("online")
    }


    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data!!

            Glide.with(this).load(filePath).into(binding.ivImageContaainer)
            binding.lnLayoutSendImage.visibility = View.VISIBLE
        }
    }


    private fun uploadImage() {

        val fileName = UUID.randomUUID().toString()
        val imageRef = storageRef.child("images/$fileName")


        imageRef.putFile(filePath)
            .addOnSuccessListener { taskSnapshot ->

                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    selectedImageUrl = uri.toString()
                    sendMessageWithImage(selectedImageUrl!!)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to upload image", exception)
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendMessageWithImage(selectedImageUrl: String) {

        val imageText = "$selectedImageUrl$IMAGE_PATH"
        sendMsg(firebaseUser.uid, userId!!, imageText)
    }
}