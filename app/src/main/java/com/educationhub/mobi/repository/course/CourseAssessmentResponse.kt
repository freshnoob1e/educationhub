package com.educationhub.mobi.repository.course

import com.educationhub.mobi.model.CourseAssessment

data class CourseAssessmentResponse(
    var course_assessment: CourseAssessment? = null,
    var exception: Exception? = null
)