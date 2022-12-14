package com.educationhub.mobi.ui.course.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.educationhub.mobi.repository.course.CourseOverviewResponse
import com.educationhub.mobi.repository.course.CourseRepository
import kotlinx.coroutines.Dispatchers

class CourseListViewModel(private val repository: CourseRepository = CourseRepository()) :
    ViewModel() {

    private var _currentCourseId = MutableLiveData<String?>("")
    val currentCourseId: LiveData<String?> get() = _currentCourseId

    private var _currentCourseIdx = MutableLiveData<Int>(0)
    val currentCourseIdx: LiveData<Int> get() = _currentCourseIdx

    val courseListResponseLiveData = liveData(Dispatchers.IO) {
        emit(repository.getCourses())
    }

    lateinit var courseOverviewResponseLiveData: LiveData<CourseOverviewResponse?>

    init {
        getCourseOverviewResponseLiveDataFunc()
    }

    fun getCourseOverviewResponseLiveDataFunc() {
        courseOverviewResponseLiveData = liveData(Dispatchers.IO) {
            emit(repository.getCourseOverview(_currentCourseId.value))
        }
    }

    fun setCurrentCourseId(newCourseId: String?) {
        _currentCourseId.value = newCourseId
    }

    fun setCurrentCourseIdx(newCourseIdx: Int) {
        _currentCourseIdx.value = newCourseIdx
    }
}