package com.educationhub.mobi.ui.course.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.UserViewModel
import com.educationhub.mobi.databinding.FragmentCourseListBinding

class CourseListFragment : Fragment() {

    private var _binding: FragmentCourseListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val courseViewModel: CourseListViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel.getCurrentUser()

        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.courseListRecyclerView
        courseViewModel.courseListResponseLiveData.observe(viewLifecycleOwner) {
            recyclerView.adapter = CourseItemAdapter(it)
        }
        recyclerView.hasFixedSize()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}