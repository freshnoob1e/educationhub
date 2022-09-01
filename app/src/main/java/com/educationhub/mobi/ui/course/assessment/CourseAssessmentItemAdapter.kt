package com.educationhub.mobi.ui.course.assessment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.R
import com.educationhub.mobi.model.AssessmentQuestion
import com.educationhub.mobi.model.CourseAssessment
import com.educationhub.mobi.model.CourseLearningOutcome
import com.educationhub.mobi.repository.course.CourseAssessmentResponse
import com.educationhub.mobi.repository.course.CourseOverviewResponse
import com.educationhub.mobi.ui.home.HomeFragmentDirections

class CourseAssessmentItemAdapter(
    courseAssessmentResponse: CourseAssessmentResponse,
    private val viewModel: CourseAssessmentViewModel
) :
    RecyclerView.Adapter<CourseAssessmentItemAdapter.ItemViewHolder>() {

    private val dataset: List<AssessmentQuestion>? =
        courseAssessmentResponse.course_assessment!!.questions

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView = view.findViewById(R.id.question_text_view)
        val radioGroup: RadioGroup = view.findViewById(R.id.answer_radio_group)
        val radioOne: RadioButton = view.findViewById(R.id.answer_option_one)
        val radioTwo: RadioButton = view.findViewById(R.id.answer_option_two)
        val radioThree: RadioButton = view.findViewById(R.id.answer_option_three)
        val radioFour: RadioButton = view.findViewById(R.id.answer_option_four)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_question, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentCourseQuestion = dataset?.get(position)
        val qText = "Q${position+1}. ${currentCourseQuestion!!.question}"
        holder.questionText.text = qText
        holder.radioOne.text = currentCourseQuestion.answers!![0].text
        holder.radioTwo.text = currentCourseQuestion.answers[1].text
        holder.radioThree.text = currentCourseQuestion.answers[2].text
        holder.radioFour.text = currentCourseQuestion.answers[3].text

        holder.radioOne.setOnClickListener {
            radioChange(
                position,
                currentCourseQuestion.answers[0].text!!
            )
        }
        holder.radioTwo.setOnClickListener {
            radioChange(
                position,
                currentCourseQuestion.answers[1].text!!
            )
        }
        holder.radioThree.setOnClickListener {
            radioChange(
                position,
                currentCourseQuestion.answers[2].text!!
            )
        }
        holder.radioFour.setOnClickListener {
            radioChange(
                position,
                currentCourseQuestion.answers[3].text!!
            )
        }
    }

    private fun radioChange(questionIdx: Int, answerText: String) {
        viewModel.setAnswer(questionIdx, answerText)
    }

    override fun getItemCount(): Int {
        if (dataset == null) {
            return 0
        }
        return dataset.size
    }


}