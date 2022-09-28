package com.educationhub.mobi.model

data class UserInfo(
    val id: String? = null,
    val courseEnrolled: List<EnrolledCourse>? = null,
    val username: String? = null,
    val fullName: String? = null,
    val avatarImageURL: String? = null,
)