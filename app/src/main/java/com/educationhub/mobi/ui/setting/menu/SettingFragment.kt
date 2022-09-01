package com.educationhub.mobi.ui.setting.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.educationhub.mobi.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.helpOption.setOnClickListener {
            val action = SettingFragmentDirections.actionNavSettingToHelpFragment()
            findNavController().navigate(action)
        }
        binding.blogOption.setOnClickListener {
            val action = SettingFragmentDirections.actionNavSettingToBlogFragment()
            findNavController().navigate(action)
        }
        binding.policyOption.setOnClickListener {
            val action = SettingFragmentDirections.actionNavSettingToNavPolicy()
            findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}