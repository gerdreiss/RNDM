package com.jscriptive.rndm.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.jscriptive.rndm.NUM_LIKES
import com.jscriptive.rndm.R
import com.jscriptive.rndm.THOUGHTS_REF
import com.jscriptive.rndm.domain.Thought
import java.text.SimpleDateFormat
import java.util.*

class ThoughtsAdapter(
        val thoughts: ArrayList<Thought>,
        val itemClick: (Thought) -> Unit
) : RecyclerView.Adapter<ThoughtsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.thought_list_view, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return thoughts.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindThought(thoughts[position])
    }

    inner class ViewHolder(itemView: View?,
                           val itemClick: (Thought) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        val username = itemView?.findViewById<TextView>(R.id.listViewUsername)
        val timestamp = itemView?.findViewById<TextView>(R.id.listViewTimestamp)
        val thoughtTxt = itemView?.findViewById<TextView>(R.id.listViewThoughtTxt)
        val numLikes = itemView?.findViewById<TextView>(R.id.listViewNumLikesLabel)
        val numComments = itemView?.findViewById<TextView>(R.id.listViewNumCommentsLabel)
        val likesImage = itemView?.findViewById<ImageView>(R.id.listViewImageView1)

        val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

        fun bindThought(thought: Thought) {
            username?.text = thought.username
            thoughtTxt?.text = thought.thoughtTxt
            numLikes?.text = thought.numLikes.toString()
            numComments?.text = thought.numComments.toString()
            timestamp?.text = dateFormatter.format(thought.timestamp)
            likesImage?.setOnClickListener {
                FirebaseFirestore.getInstance()
                        .collection(THOUGHTS_REF)
                        .document(thought.documentId)
                        .update(NUM_LIKES, thought.numLikes + 1)
            }
            itemView.setOnClickListener { itemClick(thought) }
        }
    }
}
