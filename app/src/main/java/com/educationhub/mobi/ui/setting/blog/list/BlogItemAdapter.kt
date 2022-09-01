package com.educationhub.mobi.ui.setting.blog.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.educationhub.mobi.R
import com.educationhub.mobi.model.Blog
import com.educationhub.mobi.repository.blog.BlogListResponse

class BlogItemAdapter(private val blogListResponse: BlogListResponse) :
    RecyclerView.Adapter<BlogItemAdapter.ItemViewHolder>() {

    private val dataset: List<Blog>? = blogListResponse.blogs

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.blog_title)
        val contentText: TextView = view.findViewById(R.id.blog_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_blog, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.titleText.text = dataset!![position].title
        holder.contentText.text = dataset[position].content
    }

    override fun getItemCount(): Int {
        return dataset!!.size
    }
}