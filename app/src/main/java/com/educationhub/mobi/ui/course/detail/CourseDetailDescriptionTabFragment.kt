package com.educationhub.mobi.ui.course.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.educationhub.mobi.databinding.FragmentCourseDetailDescriptionTabBinding
import com.educationhub.mobi.ui.course.list.CourseListViewModel

class CourseDetailDescriptionTabFragment : Fragment() {

    private var _binding: FragmentCourseDetailDescriptionTabBinding? = null
    private val viewModel: CourseListViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailDescriptionTabBinding.inflate(inflater, container, false)
        val root = binding.root
        val currentCourseIdx = viewModel.currentCourseIdx

        viewModel.courseListResponseLiveData.observe(viewLifecycleOwner) {
            binding.descriptionTextView.text = it.courses!![currentCourseIdx.value!!].description
            val diffText = "Difficulty: ${it.courses!![currentCourseIdx.value!!].difficulty}"
            binding.difficultyText.text = diffText
        }

        return root
    }
}