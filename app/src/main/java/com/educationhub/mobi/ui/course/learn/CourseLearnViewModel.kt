package com.educationhub.mobi.ui.course.learn

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.educationhub.mobi.repository.course.CourseContentSlidesResponse
import com.educationhub.mobi.repository.course.CourseRepository
import com.educationhub.mobi.repository.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class CourseLearnViewModel(
    private val repository: CourseRepository = CourseRepository(),
    private val userRepo: UserRepository = UserRepository()
) : ViewModel() {
    var courseContentSlideLiveData: LiveData<CourseContentSlidesResponse?>? = null
    var courseId: String? = null


    fun getCourseContentSlides(courseId: String) {
        courseContentSlideLiveData = liveData(Dispatchers.IO) {
            emit(repository.getCourseContentSlides(courseId))
        }
    }

    var maxSlide: Int = 0

    // current slide
    private val _currentSlide = MutableLiveData(1)
    val currentSlide: LiveData<Int> get() = _currentSlide

    fun increaseSlideNum() {
        if (currentSlide.value!! >= maxSlide - 1) {
            return
        }
        setCurrentSlide(currentSlide.value!! + 1)
    }

    fun decreaseSlideNum() {
        if (currentSlide.value!! <= 0) {
            return
        }
        setCurrentSlide(currentSlide.value!! - 1)
    }

    fun setCurrentSlide(slideNum: Int) {
        _currentSlide.value = slideNum
        runBlocking {
            withContext(Dispatchers.IO){
                userRepo.updateUserEnrolledCourseCurrentSlide(courseId!!, slideNum)
            }
        }
    }

    fun completeCourse(){
        setCurrentSlide(maxSlide)
    }


}