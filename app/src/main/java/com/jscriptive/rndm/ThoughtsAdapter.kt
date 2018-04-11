package com.jscriptive.rndm

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ThoughtsAdapter(val thoughts: ArrayList<Thought>) : RecyclerView.Adapter<ThoughtsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.thought_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return thoughts.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindThought(thoughts[position])
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val username = itemView?.findViewById<TextView>(R.id.listViewUsername)
        val timestamp = itemView?.findViewById<TextView>(R.id.listViewTimestamp)
        val thoughtTxt = itemView?.findViewById<TextView>(R.id.listViewThoughtTxt)
        val numLikes = itemView?.findViewById<TextView>(R.id.listViewNumLikesLabel)
        val likesImage = itemView?.findViewById<ImageView>(R.id.listViewImageView)

        val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

        fun bindThought(thought: Thought) {
            username?.text = thought.username
            thoughtTxt?.text = thought.thoughtTxt
            numLikes?.text = thought.numLikes.toString()
            timestamp?.text = dateFormatter.format(thought.timestamp)
            likesImage?.setOnClickListener {
                FirebaseFirestore.getInstance()
                        .collection(THOUGHTS_REF)
                        .document(thought.documentId)
                        .update(NUM_LIKES, thought.numLikes + 1)
            }
        }
    }
}
