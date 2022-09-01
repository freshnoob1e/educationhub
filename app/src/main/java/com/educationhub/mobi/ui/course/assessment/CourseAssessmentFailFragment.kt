package com.educationhub.mobi.ui.course.assessment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.educationhub.mobi.R
import com.educationhub.mobi.databinding.FragmentCourseAssessmentFailBinding

class CourseAssessmentFailFragment : Fragment() {
    private var _binding: FragmentCourseAssessmentFailBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseAssessmentFailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTryAgain.setOnClickListener {
            findNavController().popBackStack()
        }

        return root
    }
}