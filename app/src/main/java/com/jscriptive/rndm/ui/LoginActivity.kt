package com.jscriptive.rndm.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.jscriptive.rndm.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
    }

    fun loginLoginClicked(view: View) {
        val email = loginEmailTxt.text.toString()
        val pwd = loginPwdTxt.text.toString()

        auth.signInWithEmailAndPassword(email, pwd)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("ERROR", "Could not log in: ${exception.localizedMessage}")
                }
    }

    fun loginCreateAcctClicked(view: View) {
        startActivity(Intent(this, CreateUserActivity::class.java))
    }
}
