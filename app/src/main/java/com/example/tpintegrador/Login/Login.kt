package com.example.tpintegrador.Login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tpintegrador.MainActivity
import com.example.tpintegrador.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    companion object{
        const val PREFERENCES = "preferences"
        const val USER_ID = "user_id"
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
        context = applicationContext
        sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        firebaseAuth = Firebase.auth

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                val intent = Intent(this, MainActivity::class.java)
                sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val userId = user?.uid.toString()
                editor.putString(USER_ID, userId)
                editor.apply()
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
                    val intent = Intent(this, MainActivity::class.java)
                    sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val userId = user?.uid.toString()
                    editor.putString(USER_ID, userId)
                    editor.apply()
                    startActivity(intent)
                }else{
                    Toast.makeText(baseContext, getString(R.string.ErrorCheck), Toast.LENGTH_SHORT).show()
                }
            }
    }
}
