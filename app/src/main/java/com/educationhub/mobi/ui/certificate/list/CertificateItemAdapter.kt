package com.educationhub.mobi.ui.certificate.list

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
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
import com.educationhub.mobi.repository.certificate.CertificateResponse
import com.educationhub.mobi.repository.progress.ProgressListResponse

class CertificateItemAdapter(certificateResponse: CertificateResponse) :
    RecyclerView.Adapter<CertificateItemAdapter.ItemViewHolder>() {
    private val certificateList = certificateResponse.completedCourses

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerTextView: TextView = view.findViewById(R.id.card_header_title_text)
        val courseImageView: ImageView = view.findViewById(R.id.course_image_view)
        val continueBtn: Button = view.findViewById(R.id.btn_continue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_certificate, parent, false)
        return ItemViewHolder(adapterLayout)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentCourse = certificateList?.get(position)
        if (currentCourse != null) {
            holder.headerTextView.text = currentCourse.courseInfo?.title
            // TODO add course thumbnail
//            holder.courseImageView.load(currentCourse.courseInfo.thumbnail)
            holder.continueBtn.setOnClickListener {
                val action =
                    CertificateListFragmentDirections.actionNavCertificateListToCertificateDetailFragment(
                        currentCourse.courseInfo?.title!!,
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return certificateList!!.size
    }
}