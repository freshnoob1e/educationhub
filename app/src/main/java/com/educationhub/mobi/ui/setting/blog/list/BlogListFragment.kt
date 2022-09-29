package com.educationhub.mobi.ui.setting.blog.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.educationhub.mobi.databinding.FragmentBlogListBinding

class BlogListFragment : Fragment() {

    private var _binding: FragmentBlogListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val blogViewModel =
            ViewModelProvider(this).get(BlogListViewModel::class.java)

        _binding = FragmentBlogListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.blogRecycler
        blogViewModel.blogListResponseLiveData.observe(viewLifecycleOwner) {
            recyclerView.adapter = BlogItemAdapter(it)
        }
        recyclerView.hasFixedSize()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}