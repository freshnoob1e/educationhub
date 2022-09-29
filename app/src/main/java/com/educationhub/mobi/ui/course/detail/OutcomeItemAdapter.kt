package com.educationhub.mobi.ui.course.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.R
import com.educationhub.mobi.model.CourseLearningOutcome
import com.educationhub.mobi.repository.course.CourseOverviewResponse

class OutcomeItemAdapter(
    courseOverviewResponse: CourseOverviewResponse
) :
    RecyclerView.Adapter<OutcomeItemAdapter.ItemViewHolder>() {

    private val dataset: List<CourseLearningOutcome>? =
        courseOverviewResponse.course_overview?.course_learning_outcomes

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.outcome_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_outcome, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val outcomeText = dataset?.get(position)?.outcome
        if (outcomeText != null) {
            holder.textView.text = outcomeText
        }
    }

    override fun getItemCount(): Int {
        if (dataset == null) {
            return 0
        }
        return dataset.size
    }


}