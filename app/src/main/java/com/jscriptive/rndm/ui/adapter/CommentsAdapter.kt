package com.jscriptive.rndm.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jscriptive.rndm.R
import com.jscriptive.rndm.domain.Comment
import java.text.SimpleDateFormat
import java.util.*

class CommentsAdapter(val comments: ArrayList<Comment>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindComment(comments[position])
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val username = itemView?.findViewById<TextView>(R.id.commentListUsername)
        val date = itemView?.findViewById<TextView>(R.id.commentListTimestamp)
        val commentTxt = itemView?.findViewById<TextView>(R.id.commentListCommentTxt)

        val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

        fun bindComment(comment: Comment) {
            username?.text = comment.username
            date?.text = dateFormatter.format(comment.timestamp)
            commentTxt?.text = comment.commentTxt
        }
    }
}