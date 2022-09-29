package com.educationhub.mobi.repository.certificate

import com.educationhub.mobi.model.EnrolledCourse
import com.educationhub.mobi.model.UserInfo
import com.educationhub.mobi.repository.user.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CertificateRepository {
    private val auth = Firebase.auth

    suspend fun getCompletedCourse(): CertificateResponse {
        val response = CertificateResponse()

        val userInfo: UserInfo? = UserRepository().getUserInfo(auth.currentUser?.uid!!)

        val allEnrolledCourse = userInfo!!.courseEnrolled
        val completedCourse = mutableListOf<EnrolledCourse>()

        for (x in allEnrolledCourse!!) {
            if (x.currentSlide == x.courseNumOfSlides) {
                completedCourse.add(x)
            }
        }

        response.completedCourses = completedCourse
        return response
    }
}