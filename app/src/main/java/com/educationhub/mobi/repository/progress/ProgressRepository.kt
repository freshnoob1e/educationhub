package com.educationhub.mobi.repository.progress

import com.educationhub.mobi.model.EnrolledCourse
import com.educationhub.mobi.model.UserInfo
import com.educationhub.mobi.repository.user.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProgressRepository {
    private  val auth = Firebase.auth

    suspend fun getEnrolledCourse(): ProgressListResponse{
        val response = ProgressListResponse()

        // Get enrolled class from user info
        val userInfo: UserInfo? = UserRepository().getUserInfo(auth.currentUser?.uid!!)

        // Get only incomplete course
        val allEnrolledCourse = userInfo!!.courseEnrolled
        val incompleteCourses = mutableListOf<EnrolledCourse>()
        for(x in allEnrolledCourse!!){
            if(x.currentSlide != x.courseNumOfSlides){
                incompleteCourses.add(x)
            }
        }

        response.progress = incompleteCourses
        return response
    }
}