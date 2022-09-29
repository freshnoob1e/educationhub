package com.educationhub.mobi.repository.course

import com.educationhub.mobi.model.CourseContentSlide

data class CourseContentSlidesResponse(
    var course_contents: List<CourseContentSlide>? = null,
    var exception: Exception? = null
)