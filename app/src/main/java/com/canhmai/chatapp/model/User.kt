package com.canhmai.chatapp.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class User(
    val email: String = "",
    val userId: String = "",
    val userImage: String = "",
    val userName: String = "",
    val lastMsg: String = "default",
    val lastTime: String = "",
    val status: String = "online"
) {


}



