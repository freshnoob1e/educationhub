package com.educationhub.mobi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.educationhub.mobi.repository.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class UserViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    var currentUser: FirebaseUser? = null

    fun getCurrentUser() {
        currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser!!.getIdToken(true).addOnCompleteListener { t ->
                try {
                    t.result
                } catch (e: Exception) {
                    getCurrentUser()
                }
            }
        } else {
            signinUserAnonymous()
        }
    }

    private fun signinUserAnonymous() {
        auth.signInAnonymously()
    }

    val userInfoResponseLiveData = liveData(Dispatchers.IO) {
        emit(repository.getUserInfoResponse(currentUser!!.uid))
    }

}