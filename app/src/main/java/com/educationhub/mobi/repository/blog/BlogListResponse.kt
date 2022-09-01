package com.educationhub.mobi.repository.blog

import com.educationhub.mobi.model.Blog
import java.lang.Exception

data class BlogListResponse(
    var blogs: List<Blog>? = null,
    var exception: Exception? = null
)
