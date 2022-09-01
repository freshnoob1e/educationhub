package com.educationhub.mobi.repository.progress

import com.educationhub.mobi.model.EnrolledCourse
import java.lang.Exception

data class ProgressListResponse (
    var progress: List<EnrolledCourse>? = null,
    var exception: Exception? = null
)