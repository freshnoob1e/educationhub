package com.educationhub.mobi.repository.course

import com.educationhub.mobi.model.CourseOverview

data class CourseOverviewResponse(
    var course_overview: CourseOverview? = null,
    var exception: Exception? = null
)
