package com.educationhub.mobi.repository.user

import android.util.Log
import com.educationhub.mobi.model.EnrolledCourse
import com.educationhub.mobi.model.UserInfo
import com.educationhub.mobi.repository.course.CourseRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun getUserInfoResponse(userId: String): UserInfoResponse {
        val response = UserInfoResponse()
        val userDoc = getUserInfoDoc(userId)
        if (userDoc != null) {
            val enrolledCoursesDocs = getUserEnrolledCoursesDocs(userId)
            val enrolledCourses = getUserEnrolledCourses(enrolledCoursesDocs)

            response.UserInfo = UserInfo(
                userDoc.id,
                enrolledCourses,
                userDoc.data?.get("username") as String?,
                userDoc.data?.get("fullName") as String?,
                userDoc.data?.get("avatarImageURL") as String?
            )
        }
        return response
    }

    suspend fun getUserInfo(userId: String): UserInfo? {
        val userDoc = getUserInfoDoc(userId)
        if (userDoc != null) {
            val enrolledCoursesDocs = getUserEnrolledCoursesDocs(userId)
            val enrolledCourses = getUserEnrolledCourses(enrolledCoursesDocs)

            return UserInfo(
                userDoc.id,
                enrolledCourses,
                userDoc.data?.get("username") as String?,
                userDoc.data?.get("fullName") as String?,
                userDoc.data?.get("avatarImageURL") as String?,
            )
        }
        return null
    }

    private suspend fun getUserInfoDoc(userId: String): DocumentSnapshot? {
        var userDoc: DocumentSnapshot? = null
        try {
            val userInfoDocs =
                db.collection("users").whereEqualTo("uid", userId).get().await().documents
            if (userInfoDocs.isEmpty()) {
                userDoc = createUserInfo(userId)
            } else {
                var hasRecord = false
                for (document in userInfoDocs) {
                    if (document.data?.get("uid") == userId) {
                        hasRecord = true
                        userDoc = document
                    }
                }
                if (!hasRecord) {
                    userDoc = createUserInfo(userId)
                }
            }
        } catch (exception: Exception) {
            Log.e("User Repository", exception.message.toString())
        }
        return userDoc
    }

    private suspend fun createUserInfo(userId: String): DocumentSnapshot {
        val userData = hashMapOf(
            "uid" to userId,
        )
        db.collection("users").document(userId).set(userData, SetOptions.merge()).await()
        return db.collection("users").document(userId).get().await()
    }

    suspend fun updateUserEnroll(userId: String, courseId: String)
            : Int {
        // 1 = enroll success | 0 = already enrolled | -1 = something went wrong
        val userDoc = getUserInfoDoc(userId)

        val currentEnrolledCoursesDocs = getUserEnrolledCoursesDocs(userId)
        for (document in currentEnrolledCoursesDocs) {
            if (document.data?.get("course_id") == courseId) {
                return 0
            }
        }
        // If course not exist/started, add entry
        // course_id, course_num_of_slide, current_slide
        // // Get course info to get num of slide
        val courseNumSlide =
            CourseRepository().getCourseContent(courseId)?.data?.get("num_of_slides")
                ?: return -1

        val data = hashMapOf(
            "course_id" to courseId,
            "course_num_of_slide" to courseNumSlide,
            "current_slide" to 0
        )

        db.collection("${userDoc?.reference?.path}/enrolled_courses").add(data).await()
        return 1
    }

    // get all enrolled course documents
    private suspend fun getUserEnrolledCoursesDocs(userId: String): List<DocumentSnapshot> {
        val userDoc = getUserInfoDoc(userId)

        return db.collection("${userDoc?.reference!!.path}/enrolled_courses").get()
            .await().documents
    }

    // Get the document of specific enrolled course
    private suspend fun getUserEnrolledCourseDoc(
        userId: String,
        courseId: String
    ): DocumentSnapshot {
        val userDoc = getUserInfoDoc(userId)

        return db.collection("${userDoc?.reference!!.path}/enrolled_courses")
            .whereEqualTo("course_id", courseId).get().await().documents[0]
    }

    private suspend fun getUserEnrolledCourses(userEnrolledCoursesDocs: List<DocumentSnapshot>): List<EnrolledCourse> {
        val enrolledCourses: MutableList<EnrolledCourse> = mutableListOf()
        for (document in userEnrolledCoursesDocs) {
            val courseId = document.data?.get("course_id") as String
            val course = CourseRepository().getCourse(courseId)
            val temp = enrolledCourseDocToEnrolledCourse(document)
            temp.courseInfo = course
            enrolledCourses.add(temp)
        }
        return enrolledCourses
    }

    private fun enrolledCourseDocToEnrolledCourse(enrolledCourseDoc: DocumentSnapshot): EnrolledCourse {
        // Does not return course info
        return EnrolledCourse(
            enrolledCourseDoc.id,
            enrolledCourseDoc.data?.get("course_id") as String,
            enrolledCourseDoc.data?.get("current_slide").toString().toInt(),
            enrolledCourseDoc.data?.get("course_num_of_slide").toString().toInt(),
        )
    }

    suspend fun updateUserEnrolledCourseCurrentSlide(courseId: String, slideNum: Int) {
        val userInfoCourseDoc = getUserEnrolledCourseDoc(auth.currentUser!!.uid, courseId)
        db.document(userInfoCourseDoc.reference.path).update("current_slide", slideNum).await()
    }
}