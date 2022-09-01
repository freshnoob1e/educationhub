package com.educationhub.mobi.repository.course

import com.educationhub.mobi.model.CourseAssessment
import java.lang.Exception

data class CourseAssessmentResponse (
    var course_assessment: CourseAssessment? = null,
    var exception: Exception? = null
)