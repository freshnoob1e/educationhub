package com.educationhub.mobi.model

data class CourseOverview(
    val id: String? = null,
    val course_id: String? = null,
    val course_learning_outcomes: List<CourseLearningOutcome>? = null,
    val image: String? = null
)