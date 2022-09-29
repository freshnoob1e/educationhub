package com.educationhub.mobi.ui.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.educationhub.mobi.repository.progress.ProgressListResponse
import com.educationhub.mobi.repository.progress.ProgressRepository
import kotlinx.coroutines.Dispatchers

class ProgressViewModel(private val repository: ProgressRepository = ProgressRepository()) :
    ViewModel() {

    lateinit var progressListResponseLiveData: LiveData<ProgressListResponse?>

    init {
        getProgressListResponseLiveData()
    }

    fun getProgressListResponseLiveData() {
        progressListResponseLiveData = liveData(Dispatchers.IO) {
            emit(repository.getEnrolledCourse())
        }
    }

}