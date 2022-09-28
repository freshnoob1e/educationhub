package com.educationhub.mobi.ui.course.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.UserViewModel
import com.educationhub.mobi.databinding.FragmentCourseDetailIndexTabBinding
import com.educationhub.mobi.repository.user.UserRepository
import com.educationhub.mobi.ui.course.list.CourseListViewModel
import kotlinx.coroutines.runBlocking

class CourseDetailIndexTabFragment : Fragment() {

    private var _binding: FragmentCourseDetailIndexTabBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val courseViewModel: CourseListViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailIndexTabBinding.inflate(inflater, container, false)
        val root = binding.root

        val recyclerView: RecyclerView = binding.courseIndexRecyclerView
        courseViewModel.courseOverviewResponseLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerView.adapter = OutcomeItemAdapter(it)
            }
        }
        recyclerView.hasFixedSize()

        binding.btnEnroll.setOnClickListener { enrollCourse() }

        return root
    }

    private fun enrollCourse() {
        runBlocking {
            val status = UserRepository().updateUserEnroll(
                userViewModel.currentUser!!.uid,
                courseViewModel.currentCourseId.value!!
            )
            when (status) {
                -1 -> {
                    Toast.makeText(
                        activity,
                        "Something went wrong, try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                0 -> {
                    Toast.makeText(activity, "Course already enrolled!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(activity, "Course enrolled successfully!", Toast.LENGTH_SHORT)
                        .show()
                    val action = CourseDetailFragmentDirections.actionNavCourseDetailToNavProgress()
                    findNavController().navigate(action)
                }
            }
        }
    }
}