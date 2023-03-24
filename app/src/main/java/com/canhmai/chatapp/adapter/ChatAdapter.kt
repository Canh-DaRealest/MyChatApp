package com.canhmai.chatapp.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.canhmai.chatapp.R
import com.canhmai.chatapp.extension.auth
import com.canhmai.chatapp.model.Chat
import com.canhmai.chatapp.model.User
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.log

class ChatAdapter(
    private val context: Context,
    private val dataSet: MutableList<Chat>, private val senderImg: String
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private var firebaseUser: FirebaseUser? = null
    private val SENDER: Int = 1
    private val RECEIVER: Int = 0
    private val IMAGE_PATH = "*#%\$||{}"

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val image: CircleImageView
        val chat_image: ImageView
        val msg: TextView
        val time: TextView


        init {


            image = view.findViewById(R.id.ci_sender_image)
            chat_image = view.findViewById(R.id.iv_image_content)
            msg = view.findViewById(R.id.tv_message_content)
            time = view.findViewById(R.id.tv_message_time)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == SENDER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sender, parent, false)
            return ViewHolder(view)
        } else {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_receiver, parent, false)

            return ViewHolder(view)
        }

    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]

        holder.time.text = item.time

        if (item.msg.contains(IMAGE_PATH)) {
            val imageUrl = item.msg.replace(IMAGE_PATH, "")
            Glide.with(context).load(imageUrl).transform(RoundedCorners(30)).into(holder.chat_image)
            holder.chat_image.visibility = View.VISIBLE
            holder.msg.visibility = View.GONE

        } else {
            holder.msg.setText(item.msg)
            holder.msg.setPadding(15, 15, 15, 15)
            holder.chat_image.visibility = View.GONE
            holder.msg.visibility = View.VISIBLE
        }

        if (holder.itemViewType == RECEIVER && senderImg != "default") {
            Glide.with(context).load(senderImg).into(holder.image)
        }
        holder.msg.setOnClickListener(View.OnClickListener {

            updateItem(holder.time)
        })
        holder.chat_image.setOnClickListener(View.OnClickListener {

            updateItem(holder.time)
        })
    }

    private fun updateItem(time: TextView) {
        if (time.visibility == View.INVISIBLE) {

            time.visibility = View.VISIBLE
        } else {
            time.visibility = View.INVISIBLE
        }
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = auth.currentUser
        Log.e(TAG, "getItemViewType: ${firebaseUser!!.uid}")

        if (dataSet[position].senderId == firebaseUser!!.uid) {

            return SENDER
        } else {
            Log.e(TAG, "getItemViewType: false")
            return RECEIVER

        }
    }


}