package com.jscriptive.rndm.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jscriptive.rndm.*
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {

    lateinit var thoughtDocumentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        thoughtDocumentId = intent.getStringExtra(DOCUMENT_KEY)
    }

    fun addCommentClicked(view: View) {
        val comment = commentTxt.text.toString()
        val thoughtRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocumentId)

        FirebaseFirestore.getInstance()
                .runTransaction { transaction ->
                    val thought = transaction.get(thoughtRef)
                    val numComments = thought.getLong(NUM_COMMENTS) + 1
                    transaction.update(thoughtRef, NUM_COMMENTS, numComments)

                    val newCommentRef = thoughtRef.collection(COMMENTS_REF).document()
                    val data = mapOf(
                            Pair(COMMENT_TXT, comment),
                            Pair(TIMESTAMP, FieldValue.serverTimestamp()),
                            Pair(USERNAME, FirebaseAuth.getInstance().currentUser?.displayName)
                    )
                    transaction.set(newCommentRef, data)
                }
                .addOnSuccessListener {
                    commentTxt.setText("")
                }
                .addOnFailureListener { exception ->
                    Log.e("ERROR", "Could not add comment ${exception.localizedMessage}")
                }
    }
}
