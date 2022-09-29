package com.educationhub.mobi.ui.course.assessment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.databinding.FragmentCourseAssessmentBinding
import com.educationhub.mobi.ui.course.learn.CourseLearnViewModel

class CourseAssessmentFragment : Fragment() {

    private var _binding: FragmentCourseAssessmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val courseViewModel: CourseAssessmentViewModel by activityViewModels()
    private val learnViewModel: CourseLearnViewModel by activityViewModels()
    private val args: CourseAssessmentFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseAssessmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Recycler view
        val recyclerView: RecyclerView = binding.questionsRecycler
        courseViewModel.setCurrentCourseId(args.courseId)
        courseViewModel.getCourseAssessmentResponseLiveData()
        courseViewModel.courseAssessmentResponseLiveData?.observe(viewLifecycleOwner) {
            recyclerView.adapter = CourseAssessmentItemAdapter(it!!, courseViewModel)
            courseViewModel.setCountdownTimerAndStart((it.course_assessment?.time_limit!!.toLong()) * 1000)
        }

        //Timer
        courseViewModel.currentTime.observe(viewLifecycleOwner) {
            binding.assessmentTimeRemainingText.text = DateUtils.formatElapsedTime(it / 1000)
        }

        courseViewModel.timeOut.observe(viewLifecycleOwner) {
            if (it) {
                val action =
                    CourseAssessmentFragmentDirections.actionNavCourseAssessmentToNavCourseAssessmentFail()
                findNavController().navigate(action)
            }
        }

        //Header
        binding.courseTitleText.text = "Assessment for ${args.courseTitle}"

        //Submit button
        binding.btnSubmit.setOnClickListener {
            val result = courseViewModel.submitAnswer()
            if (result == null) {
                Toast.makeText(requireContext(), "Time out", Toast.LENGTH_SHORT).show()
                val action =
                    CourseAssessmentFragmentDirections.actionNavCourseAssessmentToNavCourseAssessmentFail()
                findNavController().navigate(action)
            } else if (result <= 0) {
                Toast.makeText(requireContext(), "Please answer all questions", Toast.LENGTH_SHORT)
                    .show()
            } else if (result <= 80) {
                val action =
                    CourseAssessmentFragmentDirections.actionNavCourseAssessmentToNavCourseAssessmentFail()
                findNavController().navigate(action)
            } else {
                learnViewModel.completeCourse()
                val action =
                    CourseAssessmentFragmentDirections.actionNavCourseAssessmentToCourseAssessmentPassFragment(
                        result,
                        args.courseTitle
                    )
                findNavController().navigate(action)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}