package com.canhmai.chatapp.model

data class ListFriendMessage(
    val name: String,
    val imageUrl: String?,
    val lastMessage: String,
    val time:String,
    val isSeen: Boolean = true
)
