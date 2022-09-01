package com.educationhub.mobi.ui.course.learn

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.educationhub.mobi.databinding.FragmentCourseLearnBinding
import com.educationhub.mobi.model.CourseContentSlide
import com.google.android.material.tabs.TabLayoutMediator

class CourseLearnFragment : Fragment() {

    private var _binding: FragmentCourseLearnBinding? = null
    private val args: CourseLearnFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: CourseLearnViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.courseId = args.courseId
        viewModel.setCurrentSlide(args.currentSlide)

        _binding = FragmentCourseLearnBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.courseTitle.text = args.courseTitle

        val viewPager: ViewPager2 = binding.courseLearnPageviewer
        val tabLayout = binding.courseSlideTabLayout

        viewModel.currentSlide.observe(viewLifecycleOwner) {
            viewPager.currentItem = it
        }

        viewModel.getCourseContentSlides(args.courseId)
        viewModel.courseContentSlideLiveData?.observe(viewLifecycleOwner) {
            viewModel.maxSlide = it?.course_contents?.size!!
            viewPager.adapter = TabAdapter(this, it.course_contents!!, args.courseTitle)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = "Lesson ${position + 1}"
            }.attach()
            Handler(Looper.getMainLooper()).postDelayed({
                viewPager.currentItem = args.currentSlide
            }, 100)
        }


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCurrentSlide(position)
                super.onPageSelected(position)
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TabAdapter(
        fragment: Fragment,
        private val dataset: List<CourseContentSlide>,
        private val courseTitle: String
    ) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = dataset.size

        override fun createFragment(position: Int): Fragment {
            if (position == itemCount - 1) {
                return CourseLearnContentFragment(dataset[position], true, courseTitle)
            }
            return CourseLearnContentFragment(dataset[position], false, courseTitle)
        }
    }
}