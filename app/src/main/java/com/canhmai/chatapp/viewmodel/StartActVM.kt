package com.canhmai.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.canhmai.chatapp.extension.auth
import com.google.firebase.auth.FirebaseUser

class StartActVM : ViewModel() {
    var userLiveData: MutableLiveData<FirebaseUser>? = MutableLiveData(auth.currentUser)

    fun updateLiveData(auth: FirebaseUser) {
        userLiveData?.postValue(auth)
    }

    fun getLiveData(): MutableLiveData<FirebaseUser>? {

        return userLiveData
    }
}