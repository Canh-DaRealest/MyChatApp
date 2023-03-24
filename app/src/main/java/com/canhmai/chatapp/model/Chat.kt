package com.canhmai.chatapp.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Chat(
    val senderId: String = "",
    val receiverId: String = "",
    val msg: String = "",
    val time:String =""
) {


}



