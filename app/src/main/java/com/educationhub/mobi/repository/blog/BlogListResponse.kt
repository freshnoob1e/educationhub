package com.educationhub.mobi.repository.blog

import com.educationhub.mobi.model.Blog

data class BlogListResponse(
    var blogs: List<Blog>? = null,
    var exception: Exception? = null
)
