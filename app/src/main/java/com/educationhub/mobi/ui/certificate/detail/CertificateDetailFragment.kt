package com.educationhub.mobi.ui.certificate.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.educationhub.mobi.databinding.FragmentCertificateDetailBinding

class CertificateDetailFragment : Fragment() {

    private var _binding: FragmentCertificateDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: CertificateDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.certTextCourseTitle.text = args.courseTitle

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
