package com.educationhub.mobi.model

data class UserInfo(
    val id: String? = null,
    val courseEnrolled: List<EnrolledCourse>? = null
)