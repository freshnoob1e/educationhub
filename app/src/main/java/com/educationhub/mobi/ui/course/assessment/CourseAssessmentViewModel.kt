package com.educationhub.mobi.ui.course.assessment

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.educationhub.mobi.repository.course.CourseAssessmentResponse
import com.educationhub.mobi.repository.course.CourseRepository
import kotlinx.coroutines.Dispatchers
import kotlin.math.roundToInt

class CourseAssessmentViewModel(private val repository: CourseRepository = CourseRepository()) :
    ViewModel() {

    var courseAssessmentResponseLiveData: LiveData<CourseAssessmentResponse?>? = null

    private val _currentCourseId = MutableLiveData("")
    val currentCourseId: LiveData<String?> get() = _currentCourseId

    fun getCourseAssessmentResponseLiveData() {
        courseAssessmentResponseLiveData = liveData(Dispatchers.IO) {
            emit(repository.getCourseAssessment(currentCourseId.value!!))
        }
    }

    fun setCurrentCourseId(courseId: String) {
        _currentCourseId.value = courseId
    }

    // TIMER
    private var _countdownTimer = 0L
    private val ONE_SECOND = 1000L
    private val DONE = 0L
    var timer: CountDownTimer? = null
    val currentTime = MutableLiveData<Long>()
    val timeOut: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun setCountdownTimerAndStart(time: Long) {
        if(timer != null){
            timer!!.cancel()
        }
        _countdownTimer = time
        timeOut.value = false
        timer = object : CountDownTimer(_countdownTimer, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                timeOut.value = true
                currentTime.value = DONE
            }
        }
        timer?.start()
    }

    // Answers
    private var answers: MutableMap<Int, String> = mutableMapOf()

    fun submitAnswer(): Float? {
        val questions = courseAssessmentResponseLiveData?.value?.course_assessment?.questions
        val numOfQuestions = questions?.size
        var correctQuestion = 0
        if (currentTime.value!! <= 0 || timeOut.value!!) {
            return null
        } else if (answers.size != numOfQuestions) {
            return -1f
        }
        answers.forEach {
            for (answer in questions[it.key].answers!!) {
                if (answer.correct && answer.text == it.value) {
                    correctQuestion += 1
                    break
                }
            }
        }
        return ((correctQuestion / numOfQuestions.toFloat())*100).roundToInt().toFloat()
    }

    fun setAnswer(questionIdx: Int, answerString: String){
        answers[questionIdx] = answerString
    }

    override fun onCleared() {
        timer?.cancel()
        super.onCleared()
    }
}