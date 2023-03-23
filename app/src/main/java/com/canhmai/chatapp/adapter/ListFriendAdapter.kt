package com.canhmai.chatapp.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.canhmai.chatapp.R
import com.canhmai.chatapp.model.ListFriendMessage
import com.google.android.material.card.MaterialCardView
import de.hdodenhof.circleimageview.CircleImageView

class ListFriendAdapter(
    private val context: Context,
    private val dataSet: List<ListFriendMessage>
) :
    RecyclerView.Adapter<ListFriendAdapter.ViewHolder>() {

    val friendLivedata: MutableLiveData<ListFriendMessage> = MutableLiveData<ListFriendMessage>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: CircleImageView
        val name: TextView
        val lastMsg: TextView
        val time: TextView
        val background: MaterialCardView

        init {
            // Define click listener for the ViewHolder's View.
            image = view.findViewById(R.id.ci_item_friend_image)
            name = view.findViewById(R.id.tv_item_friend_name)
            lastMsg = view.findViewById(R.id.tv_item_friend_last_message)
            time = view.findViewById(R.id.tv_item_friend_message_time)
            background = view.findViewById(R.id.cv_item_frieng_msg)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message_list, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = dataSet.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        if (item.imageUrl != null) {
            Glide.with(context).load(item.imageUrl).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_user)
        }

        holder.lastMsg.text = item.lastMessage
        holder.time.text = item.time
        if (item.isSeen) {
            holder.lastMsg.typeface = Typeface.DEFAULT_BOLD;
            holder.time.typeface = Typeface.DEFAULT_BOLD;
        }
        holder.background.setOnClickListener(View.OnClickListener { onClickItem(item) })
    }

    private fun onClickItem(friend: ListFriendMessage) {
        friendLivedata.postValue(friend)
    }


}