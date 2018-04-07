package com.jscriptive.rndm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_thought.*

class AddThoughtActivity : AppCompatActivity() {

    var selectedCategory: String = FUNNY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_thought)
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
        val data = mapOf(
                Pair("category", selectedCategory),
                Pair("numComments", 0),
                Pair("numLikes", 0),
                Pair("thought", addThoughtTxt.text.toString()),
                Pair("timestamp", FieldValue.serverTimestamp()),
                Pair("username", addUsernameTxt.text.toString())
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
