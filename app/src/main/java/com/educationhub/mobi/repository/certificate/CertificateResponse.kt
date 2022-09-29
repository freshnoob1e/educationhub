package com.educationhub.mobi.repository.certificate

import com.educationhub.mobi.model.EnrolledCourse

data class CertificateResponse(
    var completedCourses: List<EnrolledCourse>? = null,
    var exception: Exception? = null
)