package com.educationhub.mobi.model

data class AssessmentQuestion(
    val question: String? = null,
    val answers: List<AssessmentAnswer>? = null,
)