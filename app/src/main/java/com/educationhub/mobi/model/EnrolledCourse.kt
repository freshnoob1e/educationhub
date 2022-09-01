package com.educationhub.mobi.model

import com.educationhub.mobi.model.Course

data class EnrolledCourse (
    var id: String? = null,
    var courseId: String? = null,
    var currentSlide: Int? = null,
    var courseNumOfSlides: Int? = null,
    var courseInfo: Course? = null
)