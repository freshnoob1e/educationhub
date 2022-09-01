package com.educationhub.mobi.ui.course.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.educationhub.mobi.R
import com.educationhub.mobi.databinding.FragmentCourseDetailBinding
import com.educationhub.mobi.model.CourseOverview
import com.educationhub.mobi.repository.course.CourseOverviewResponse
import com.educationhub.mobi.ui.course.list.CourseListViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CourseDetailFragment : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: CourseDetailFragmentArgs by navArgs()

    private val courseViewModel: CourseListViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        courseViewModel.getCourseOverviewResponseLiveDataFunc()

        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val courseTitleTextView: TextView = binding.courseTitleText

        var courseDescription: String = ""
        val currentCourseId =
            courseViewModel.courseListResponseLiveData.value!!.courses!![args.courseIdx].id
        courseViewModel.setCurrentCourseId(currentCourseId)
        courseViewModel.setCurrentCourseIdx(args.courseIdx)

        courseViewModel.courseListResponseLiveData.observe(viewLifecycleOwner) {
            courseTitleTextView.text = it.courses!![args.courseIdx].title
            courseDescription = it.courses!![args.courseIdx].description.toString()
        }

        courseViewModel.courseOverviewResponseLiveData.observe(viewLifecycleOwner) {
            if (it?.course_overview != null) {
                binding.courseImage.load(it.course_overview!!.image)
                binding.courseImageLoader.visibility = View.GONE
                if (it.course_overview!!.image == null) {
                    binding.courseImageLoader.visibility = View.GONE
                }
            } else if (it?.exception != null) {
                binding.courseImage.setImageResource(R.drawable.image_placeholder)
                binding.courseImageLoader.visibility = View.GONE
            } else {
                binding.courseImageLoader.visibility = View.VISIBLE
            }
        }

        val viewPager: ViewPager2 = binding.courseOverviewPageviewer
        viewPager.adapter = TabAdapter(this)
        val tabLayout: TabLayout = binding.courseOverviewTabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            var toSetText: String = ""
            toSetText = when (position) {
                0 -> getString(R.string.description)
                1 -> getString(R.string.index)
                else -> ""
            }
            tab.text = toSetText
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CourseDetailDescriptionTabFragment()
                else -> CourseDetailIndexTabFragment()
            }
        }

    }
}