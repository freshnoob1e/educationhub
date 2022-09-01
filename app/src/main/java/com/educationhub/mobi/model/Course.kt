package com.educationhub.mobi.model

data class Course(
    var id: String? = null,
    var title: String? = null,
    val difficulty: Int? = null,
    var description: String? = null,
    val started: Boolean? = null,
    var course_learning_outcomes: List<CourseLearningOutcome>? = null
)