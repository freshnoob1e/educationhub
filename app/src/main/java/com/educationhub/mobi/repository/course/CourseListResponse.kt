package com.educationhub.mobi.repository.course

import com.educationhub.mobi.model.Course
import java.lang.Exception

data class CourseListResponse (
    var courses: List<Course>? = null,
    var exception: Exception? = null,
)