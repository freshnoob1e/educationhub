package com.educationhub.mobi.repository.course

import com.educationhub.mobi.model.Course

data class CourseListResponse(
    var courses: List<Course>? = null,
    var exception: Exception? = null,
)