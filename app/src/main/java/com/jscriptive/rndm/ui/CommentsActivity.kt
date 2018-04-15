package com.jscriptive.rndm.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jscriptive.rndm.*
import com.jscriptive.rndm.domain.Comment
import com.jscriptive.rndm.ui.adapter.CommentsAdapter
import kotlinx.android.synthetic.main.activity_comments.*
import java.util.*

class CommentsActivity : AppCompatActivity(), CommentsOptionsClickListener {

    lateinit var thoughtDocumentId: String
    lateinit var commentsAdapter: CommentsAdapter
    val comments = arrayListOf<Comment>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        thoughtDocumentId = intent.getStringExtra(DOCUMENT_KEY)
        commentsAdapter = CommentsAdapter(comments, this)
        commentListView.adapter = commentsAdapter
        commentListView.layoutManager = LinearLayoutManager(this)

        FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocumentId)
                .collection(COMMENTS_REF)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Log.e("ERROR", "Could not add comment ${exception.localizedMessage}")
                    }
                    if (snapshot != null) {
                        val commentObjects = snapshot.documents.map { document ->
                            Comment(username = document.data[USERNAME] as String,
                                    timestamp = document.data[TIMESTAMP] as Date,
                                    commentTxt = document.data[COMMENT_TXT] as String,
                                    documentId = thoughtDocumentId,
                                    userId = document.data[USER_ID] as String)
                        }
                        comments.clear()
                        comments.addAll(commentObjects)
                        commentsAdapter.notifyDataSetChanged()
                    }
                }
    }

    override fun commentOptionsMenuClicked(comment: Comment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val data = mapOf(
                            Pair(COMMENT_TXT, comment),
                            Pair(TIMESTAMP, FieldValue.serverTimestamp()),
                            Pair(USERNAME, currentUser?.displayName),
                            Pair(USER_ID, currentUser?.uid)
                    )
                    transaction.set(newCommentRef, data)
                }
                .addOnSuccessListener {
                    commentTxt.setText("")
                    hideKeyboard()
                }
                .addOnFailureListener { exception ->
                    Log.e("ERROR", "Could not add comment ${exception.localizedMessage}")
                }
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
