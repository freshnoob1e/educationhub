package com.educationhub.mobi.ui.progress

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.R
import com.educationhub.mobi.repository.progress.ProgressListResponse

class ProgressItemAdapter(progressListResponse: ProgressListResponse) :
    RecyclerView.Adapter<ProgressItemAdapter.ItemViewHolder>() {
    private val progressList = progressListResponse.progress

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val headerTextView: TextView = view.findViewById(R.id.card_header_title_text)
        val progressPercentageTextView: TextView = view.findViewById(R.id.progress_percentage_text)
        val courseImageView: ImageView = view.findViewById(R.id.course_image_view)
        val mainTitleTextView: TextView = view.findViewById(R.id.text_view_course_title)
        val continueBtn: Button = view.findViewById(R.id.btn_continue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_progress, parent, false)
        return ItemViewHolder(adapterLayout)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentProgress = progressList?.get(position)
        if (currentProgress != null) {
            holder.headerTextView.text = currentProgress.courseInfo?.title
            val currentProgressPercentage: Int =
                ((currentProgress.currentSlide!!.toFloat() / currentProgress.courseNumOfSlides!!.toFloat()) * 100).toInt()
            holder.progressPercentageTextView.text = "${currentProgressPercentage}% Completed"
            // TODO add course thumbnail
//            holder.courseImageView.load(currentProgress.courseInfo.thumbnail)
            holder.mainTitleTextView.text = currentProgress.courseInfo?.title
            holder.continueBtn.setOnClickListener {
                val action = ProgressFragmentDirections.actionNavProgressToNavCourseLearn(
                    currentProgress.courseInfo?.id!!,
                    currentProgress.courseInfo?.title!!,
                    currentProgress.currentSlide!!
                )
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return progressList!!.size
    }

}