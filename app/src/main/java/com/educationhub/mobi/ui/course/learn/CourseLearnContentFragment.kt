package com.educationhub.mobi.ui.course.learn

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.educationhub.mobi.databinding.FragmentCourseLearnContentBinding
import com.educationhub.mobi.model.CourseContentSlide

class CourseLearnContentFragment(
    private val courseContent: CourseContentSlide,
    private val isLastSlide: Boolean,
    private val courseTitle: String
) : Fragment() {

    private var _binding: FragmentCourseLearnContentBinding? = null

    private val binding get() = _binding!!

    private val viewModel: CourseLearnViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseLearnContentBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.contentImageView.load(courseContent.image)
        binding.contentTextView.text = courseContent.content
        binding.slideTitleTextView.text = courseContent.title
        binding.timeToFinishText.text = "${courseContent.minToFinish} minutes"

        if (isLastSlide) {
            binding.btnContinue.text = "Start Assessment"
            binding.btnContinue.setOnClickListener {
                val action =
                    CourseLearnFragmentDirections.actionCourseLearnFragmentToCourseAssessmentFragment(
                        viewModel.courseId!!,
                        courseTitle
                    )
                findNavController().navigate(action)
            }
        } else {
            binding.btnContinue.setOnClickListener {
                viewModel.increaseSlideNum()
            }
        }

        return root
    }

}