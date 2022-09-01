package com.educationhub.mobi.ui.certificate.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.databinding.FragmentCertificateListBinding

class CertificateListFragment : Fragment() {

    private var _binding: FragmentCertificateListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val certificateViewModel: CertificateListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView: RecyclerView = binding.completedCourseRecycler
        certificateViewModel.getCertificateResponse()
        certificateViewModel.certResponseLiveData.observe(viewLifecycleOwner) {
            if (it!!.completedCourses == null || it.completedCourses!!.isEmpty()) {
                binding.emptyCourseText.visibility = View.VISIBLE
            } else {
                binding.emptyCourseText.visibility = View.GONE
                recyclerView.adapter = CertificateItemAdapter(it)
            }
        }
        recyclerView.hasFixedSize()

        binding.btnStartLearning.setOnClickListener {
            val action = CertificateListFragmentDirections.actionNavCertificateListToNavProgress()
            findNavController().navigate(action)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}