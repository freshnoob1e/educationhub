package com.educationhub.mobi.ui.course.assessment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.educationhub.mobi.databinding.FragmentCourseAssessmentPassBinding

class CourseAssessmentPassFragment : Fragment() {
    private var _binding: FragmentCourseAssessmentPassBinding? = null

    private val binding get() = _binding!!
    private val args: CourseAssessmentPassFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseAssessmentPassBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val text = "You scored: ${args.score}%"

        binding.scoreTextView.text = text

        binding.button.setOnClickListener {
            val action =
                CourseAssessmentPassFragmentDirections.actionNavCourseAssessmentPassToNavCertificateDetail(
                    args.courseTitle
                )
            findNavController().navigate(action)
        }

        return root
    }
}