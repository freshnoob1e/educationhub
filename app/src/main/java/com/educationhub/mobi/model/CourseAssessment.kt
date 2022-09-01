package com.educationhub.mobi.model

data class CourseAssessment (
    val id: String? = null,
    val course_id: String? = null,
    val time_limit: Int = 0,
    val questions: List<AssessmentQuestion>? = null
)