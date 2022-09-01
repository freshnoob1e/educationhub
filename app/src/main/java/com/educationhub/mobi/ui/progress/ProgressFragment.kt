package com.educationhub.mobi.ui.progress

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.databinding.FragmentProgressBinding

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        progressViewModel.getProgressListResponseLiveData()
        progressViewModel.progressListResponseLiveData.observe(viewLifecycleOwner) {
            if (it!!.progress == null || it.progress!!.isEmpty()) {
                binding.emptyCourseText.visibility = View.VISIBLE
            } else {
                binding.emptyCourseText.visibility = View.GONE
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