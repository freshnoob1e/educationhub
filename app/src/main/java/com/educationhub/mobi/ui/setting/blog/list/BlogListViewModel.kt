package com.educationhub.mobi.ui.setting.blog.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.educationhub.mobi.repository.blog.BlogListResponse
import com.educationhub.mobi.repository.blog.BlogRepository
import kotlinx.coroutines.Dispatchers

class BlogListViewModel(private val repository: BlogRepository = BlogRepository()) : ViewModel() {

    var blogListResponseLiveData = liveData(Dispatchers.IO){
        emit(repository.getBlog())
    }
}