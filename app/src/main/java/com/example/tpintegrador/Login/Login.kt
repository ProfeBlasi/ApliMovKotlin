package com.example.tpintegrador.Login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tpintegrador.PageCourse
import com.example.tpintegrador.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    companion object{
        const val USER_ID = "USER_ID"
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnToAccess : Button = findViewById(R.id.btnToAccess)
        val edtEmail : TextView = findViewById(R.id.edtEmail)
        val edtPassword : TextView = findViewById(R.id.edtPassword)
        val btnCreateAccount : TextView = findViewById(R.id.btnCreateAccount)
        val btnForgotMyPassword : TextView = findViewById(R.id.btnForgotMyPassword)
        firebaseAuth = Firebase.auth

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                val intent = Intent(this, PageCourse::class.java)
                intent.putExtra(USER_ID, user.uid)
                startActivity(intent)
            }
        }

        btnToAccess.setOnClickListener() {
            signIn(edtEmail.text.toString(), edtPassword.text.toString())
        }
        btnCreateAccount.setOnClickListener() {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
        btnForgotMyPassword.setOnClickListener(){
            val act = Intent(this, RememberPassword::class.java)
            startActivity(act)
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    val intent = Intent(this, PageCourse::class.java)
                    intent.putExtra(USER_ID, user?.uid.toString())
                    startActivity(intent)
                }else{
                    Toast.makeText(baseContext, getString(R.string.ErrorCheck), Toast.LENGTH_SHORT).show()
                }
            }
    }
}