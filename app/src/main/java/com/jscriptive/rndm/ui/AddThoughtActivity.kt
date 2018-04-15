package com.jscriptive.rndm.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jscriptive.rndm.*
import kotlinx.android.synthetic.main.activity_add_thought.*

class AddThoughtActivity : AppCompatActivity() {

    lateinit var selectedCategory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_thought)
        selectedCategory = intent.getStringExtra(SELECTED_CATEGORY)
        addFunnyBtn.isChecked = selectedCategory == FUNNY
        addSeriousBtn.isChecked = selectedCategory == SERIOUS
        addCrazyBtn.isChecked = selectedCategory == CRAZY

    }

    fun addFunnyClicked(view: View) {
        if (selectedCategory == FUNNY) {
            addFunnyBtn.isChecked = true
        } else {
            addSeriousBtn.isChecked = false
            addCrazyBtn.isChecked = false
            selectedCategory = FUNNY
        }
    }

    fun addSeriousClicked(view: View) {
        if (selectedCategory == SERIOUS) {
            addSeriousBtn.isChecked = true
        } else {
            addFunnyBtn.isChecked = false
            addCrazyBtn.isChecked = false
            selectedCategory = SERIOUS
        }
    }

    fun addCrazyClicked(view: View) {
        if (selectedCategory == CRAZY) {
            addCrazyBtn.isChecked = true
        } else {
            addFunnyBtn.isChecked = false
            addSeriousBtn.isChecked = false
            selectedCategory = CRAZY
        }
    }

    fun addPostClicked(view: View) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val data = mapOf(
                Pair(CATEGORY, selectedCategory),
                Pair(NUM_COMMENTS, 0),
                Pair(NUM_LIKES, 0),
                Pair(THOUGHT_TXT, addThoughtTxt.text.toString()),
                Pair(TIMESTAMP, FieldValue.serverTimestamp()),
                Pair(USERNAME, currentUser?.displayName),
                Pair(USER_ID, currentUser?.uid)
        )

        FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
                .add(data)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception", "Could not add post: $exception")
                }
    }

}
