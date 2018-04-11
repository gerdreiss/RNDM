package com.jscriptive.rndm.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jscriptive.rndm.DATE_CREATED
import com.jscriptive.rndm.R
import com.jscriptive.rndm.USERNAME
import com.jscriptive.rndm.USERS_REF
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        auth = FirebaseAuth.getInstance()
    }

    fun createCreateAcctClicked(view: View) {
        val email = createEmailTxt.text.toString()
        val pwd = createPwdTxt.text.toString()
        val username = createUsernameTxt.text.toString()
        // TODO validate input

        auth.createUserWithEmailAndPassword(email, pwd)
                .addOnSuccessListener { result ->
                    val changeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()
                    result.user.updateProfile(changeRequest)
                            .addOnFailureListener { exception ->
                                Log.e("ERROR", "Could not update display name: ${exception.localizedMessage}")
                            }

                    val data = mapOf(
                            Pair(USERNAME, username),
                            Pair(DATE_CREATED, FieldValue.serverTimestamp())
                    )
                    FirebaseFirestore.getInstance().collection(USERS_REF).document(result.user.uid)
                            .set(data)
                            .addOnSuccessListener {
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                Log.e("ERROR", "Could not add user document: ${exception.localizedMessage}")
                            }
                }
                .addOnFailureListener { exception ->
                    Log.e("ERROR", "Could not create user: ${exception.localizedMessage}")
                }
    }

    fun createCancelClicked(view: View) {
        finish()
    }
}
