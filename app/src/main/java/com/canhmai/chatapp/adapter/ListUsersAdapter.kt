package com.canhmai.chatapp.adapter

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.canhmai.chatapp.R
import com.canhmai.chatapp.extension.auth
import com.canhmai.chatapp.model.Chat
import com.canhmai.chatapp.model.User
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class ListUsersAdapter(
    private val context: Context,
    private val dataSet: MutableList<User>
) :
    RecyclerView.Adapter<ListUsersAdapter.ViewHolder>() {
    private val IMAGE_PATH = "*#%\$||{}"
    val userLiveData: MutableLiveData<User> = MutableLiveData<User>()
    lateinit var firebaseUser: FirebaseUser
    lateinit var reference: DatabaseReference

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: CircleImageView
        val name: TextView
        val background: MaterialCardView
        val itemOnline: View

        init {
            // Define click listener for the ViewHolder's View.
            image = view.findViewById(R.id.ci_item_friend_image)
            name = view.findViewById(R.id.tv_item_friend_name)

            background = view.findViewById(R.id.cv_item_frieng_msg)
            itemOnline = view.findViewById(R.id.v_item_online)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = dataSet.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        if (item.userImage != "default") {
            Glide.with(context).load(item.userImage).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_user)
        }

        Log.e("TAG", "onBindViewHolder: ${item.userId}")

        holder.name.text = item.userName




        if (item.status.equals("online")) {
            holder.itemOnline.visibility = View.VISIBLE
        } else {
            holder.itemOnline.visibility = View.INVISIBLE
        }


        holder.background.setOnClickListener(View.OnClickListener { onClickItem(item) })
    }

    private fun getLastMsg(userId: String, lastMsg: TextView, time: TextView) {
        firebaseUser = auth.currentUser!!
        Log.e("TAG", "getLastMsg: ${firebaseUser.uid}")
        reference = FirebaseDatabase.getInstance().getReference("Chat")
        var value = false


        lastMsg.text = "Chưa có cuộc trò chuyện"
        time.text = ""

        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    Log.e("TAG", "onDataChange: ${chat!!.senderId} ,  ${chat.receiverId}")

                    if (chat.senderId == userId && chat.receiverId.equals(firebaseUser)
                        ||
                        chat.receiverId == userId && chat.senderId.equals(firebaseUser)
                    ) {

                        if (chat.msg.contains(IMAGE_PATH)) {
                            lastMsg.text = "đã gửi một ảnh"
                        } else {
                            lastMsg.text = chat.msg
                        }
                        time.text = chat.time
                        value = true
                        break
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun onClickItem(friend: User) {
        userLiveData.postValue(friend)
    }


}