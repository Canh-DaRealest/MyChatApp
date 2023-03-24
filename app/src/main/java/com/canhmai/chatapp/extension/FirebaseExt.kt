package com.canhmai.chatapp.extension

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

inline val auth: FirebaseAuth
    get() = Firebase.auth




