package com.educationhub.mobi.repository.course

import com.educationhub.mobi.model.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class CourseRepository {
    private val db = Firebase.firestore

    suspend fun getCourses(): CourseListResponse {
        val response = CourseListResponse()
        try {
            val documents = db.collection("courses").get().await().documents
            val courses = mutableListOf<Course>()

            for (document in documents) {
                courses.add(
                    Course(
                        document.id,
                        document.data?.get("title") as String,
                        (document.data?.get("difficulty") as Number).toInt(),
                        document.data?.get("description") as String,
                        document.data?.get("thumbnail") as String
                    )
                )
            }
            response.courses = courses
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }

    suspend fun getCourse(courseId: String): Course {
        val doc = db.document("/courses/$courseId").get().await()
        return Course(
            doc.id,
            doc.data?.get("title") as String,
            doc.data?.get("difficulty").toString().toInt(),
            doc.data?.get("description") as String,
            doc.data?.get("thumbnail") as String,
            null
        )
    }

    suspend fun getCourseOverview(courseId: String?): CourseOverviewResponse? {
        if (courseId.isNullOrEmpty()) {
            return null
        }
        val response = CourseOverviewResponse()
        try {
            val temp = db.collection("course_overview").get().await().documents
            var hasDoc = false

            for (document in temp) {
                if (document.data?.get("course") as String == courseId) {
                    val outcomeDocs =
                        db.collection("/course_overview/${document.id}/course_learning_outcome")
                            .get().await().documents
                    val courseLearningOutcomes: List<CourseLearningOutcome> =
                        courseLearningOutcomeDocsToList(outcomeDocs)

                    val courseOverview = CourseOverview(
                        document.id,
                        courseId,
                        courseLearningOutcomes,
                        document.data?.get("image") as String
                    )
                    response.course_overview = courseOverview
                    hasDoc = true
                    break
                }
            }
            if (!hasDoc) {
                throw Exception("No course overview found")
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }

    private fun courseLearningOutcomeDocsToList(documents: MutableList<DocumentSnapshot>): List<CourseLearningOutcome> {
        val learningOutcomes: MutableList<CourseLearningOutcome> =
            mutableListOf()

        for (document in documents) {
            val outcome = CourseLearningOutcome(
                document.id,
                document.data?.get("outcome") as String
            )
            learningOutcomes.add(outcome)
        }

        return learningOutcomes.toList()
    }

    suspend fun getCourseContent(courseId: String): DocumentSnapshot? {
        val courseContentDocs =
            db.collection("course_contents").whereEqualTo("course_id", courseId).get()
                .await().documents

        return courseContentDocs[0]
    }


    suspend fun getCourseContentSlides(courseId: String): CourseContentSlidesResponse {
        val courseContentSlideResponse = CourseContentSlidesResponse()
        try {
            val courseContentDoc = getCourseContent(courseId)
            val courseContentSlidesDocs =
                db.collection("/course_contents/${courseContentDoc?.id}/slides").get()
                    .await().documents
            // Make list of course content slide
            val courseContentSlides: MutableList<CourseContentSlide> = mutableListOf()
            for (document in courseContentSlidesDocs) {
                courseContentSlides.add(
                    CourseContentSlide(
                        document.id,
                        (document.data?.get("slide_num").toString()).toInt(),
                        document.data?.get("title") as String,
                        document.data?.get("image") as String,
                        (document.data?.get("min_to_finish").toString()).toInt(),
                        document.data?.get("content") as String
                    )
                )
            }
            // Sort slides
            courseContentSlides.sortBy { it.slideNum }
            courseContentSlideResponse.course_contents = courseContentSlides.toList()
        } catch (exception: Exception) {
            courseContentSlideResponse.exception = exception
        }
        return courseContentSlideResponse
    }

    suspend fun getCourseAssessment(courseId: String): CourseAssessmentResponse {
        val response = CourseAssessmentResponse()

        try {
            val courseAssessmentDoc =
                db.collection("course_assessments").whereEqualTo("course_id", courseId).get()
                    .await().documents[0]
            val questions = courseAssessmentDoc.data?.get("questions") as List<Map<*, *>>
            val questionsList: MutableList<AssessmentQuestion> = mutableListOf()
            questions.forEach {
                val answers = it["answers"] as List<Map<*, *>>
                val answersList: MutableList<AssessmentAnswer> = mutableListOf()
                answers.forEach { map ->
                    val ans = AssessmentAnswer(map["text"] as String, map["correct"] as Boolean)
                    answersList.add(ans)
                }
                val question = AssessmentQuestion(it["question"] as String, answersList)
                questionsList.add(question)
            }

            response.course_assessment = CourseAssessment(
                courseAssessmentDoc.id,
                courseAssessmentDoc.data?.get("course_id") as String,
                courseAssessmentDoc.data?.get("time_limit").toString().toInt(),
                questionsList
            )
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }
}