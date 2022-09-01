package com.educationhub.mobi.ui.course.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.R
import com.educationhub.mobi.model.Course
import com.educationhub.mobi.repository.course.CourseListResponse
import com.educationhub.mobi.ui.home.HomeFragmentDirections

class CourseItemAdapter(
    courseResponse: CourseListResponse
) :
    RecyclerView.Adapter<CourseItemAdapter.ItemViewHolder>() {

    private val dataset: List<Course>? = courseResponse.courses

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.course_card)
        val titleTextView: TextView = view.findViewById(R.id.course_title_text)
        val courseThumbnailImgView: ImageView = view.findViewById(R.id.course_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_course, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentCourse = dataset?.get(position)
        if (currentCourse != null) {
            holder.titleTextView.text = currentCourse.title
        }
        holder.cardView.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToNavCourseDetail(
                courseIdx = position
            )
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dataset!!.size
    }


}