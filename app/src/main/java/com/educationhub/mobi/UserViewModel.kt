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

class UserViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    var currentUser: FirebaseUser? = null

    //    Get current user, if have user, check if user is anonymous
    //    if user is not authenticated, login user as anonymous and return false,
    //    else set user and return true
    fun getCurrentUser(): Boolean {
        currentUser = auth.currentUser
        return if (currentUser != null) {
            currentUser!!.getIdToken(true).addOnCompleteListener { t ->
                try {
                    t.result
                } catch (e: Exception) {
                    getCurrentUser()
                }
            }
            if (currentUser!!.isAnonymous) {
                return false
            }
            true
        } else {
            signinUserAnonymous()
            false
        }
    }

    private fun signinUserAnonymous() {
        auth.signInAnonymously().addOnCompleteListener {
            if (it.exception != null) {
                Log.e("AnonymousSignIn", it.exception.toString())
            }
            currentUser = auth.currentUser
        }
    }

    val userInfoResponseLiveData = liveData(Dispatchers.IO) {
        emit(repository.getUserInfoResponse(currentUser!!.uid))
    }

    fun logoutUser() {
        auth.signOut()
    }
}