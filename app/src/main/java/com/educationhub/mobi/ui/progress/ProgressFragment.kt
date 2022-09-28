package com.educationhub.mobi.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.databinding.FragmentProgressBinding
import com.educationhub.mobi.repository.progress.ProgressListResponse

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null

    private val binding get() = _binding!!
    private val progressViewModel: ProgressViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.enrolledCourseRecycler
        recyclerView.adapter = ProgressItemAdapter(ProgressListResponse())
        val loader: ProgressBar = binding.courseImageLoader
        progressViewModel.getProgressListResponseLiveData()
        progressViewModel.progressListResponseLiveData.observe(viewLifecycleOwner) {
            if (it!!.progress == null || it.progress!!.isEmpty()) {
                binding.emptyCourseText.visibility = View.VISIBLE
                loader.visibility = View.GONE
            } else {
                binding.emptyCourseText.visibility = View.GONE
                loader.visibility = View.GONE
                recyclerView.adapter = ProgressItemAdapter(it)
            }
        }
        recyclerView.hasFixedSize()

        binding.btnAddNewCourse.setOnClickListener {
            val action = ProgressFragmentDirections.actionNavProgressToNavHome()
            findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}