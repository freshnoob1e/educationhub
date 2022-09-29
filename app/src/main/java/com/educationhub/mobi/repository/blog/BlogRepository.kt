package com.educationhub.mobi.repository.blog

import com.educationhub.mobi.model.Blog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class BlogRepository {
    private val db = Firebase.firestore


    suspend fun getBlog(): BlogListResponse {
        val response = BlogListResponse()

        try {
            val docs = db.collection("blogs").get().await().documents
            val blogList = mutableListOf<Blog>()
            for (doc in docs) {
                blogList.add(
                    Blog(
                        doc.data?.get("title") as String,
                        doc.data?.get("content") as String
                    )
                )
            }
            response.blogs = blogList.toList()
        } catch (e: Exception) {
            response.exception = e
        }
        return response
    }


}