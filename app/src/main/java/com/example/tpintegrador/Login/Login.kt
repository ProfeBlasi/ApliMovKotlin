package com.example.tpintegrador.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tpintegrador.HiTeacher
import com.example.tpintegrador.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnIngresar : Button = findViewById(R.id.btnIngresar)
        val txtEmail : TextView = findViewById(R.id.edtEmail)
        val txtPass : TextView = findViewById(R.id.edtPassword)
        val btnCrearCuenta : TextView = findViewById(R.id.btn_crear_cuenta)
        val btnForgotMyPassword : TextView = findViewById(R.id.btnForgotMyPassword)
        firebaseAuth = Firebase.auth
        btnIngresar.setOnClickListener() {
            signIn(txtEmail.text.toString(), txtPass.text.toString())
        }
        btnCrearCuenta.setOnClickListener() {
            val act = Intent(this, CreateAccountActivity::class.java)
            startActivity(act)
        }
        btnForgotMyPassword.setOnClickListener(){
            val act = Intent(this, RememberPassword::class.java)
            startActivity(act)
        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    val act = Intent(this, HiTeacher::class.java)
                    startActivity(act)
                }else{
                    Toast.makeText(baseContext, "ERROR, verifique sus credenciales", Toast.LENGTH_SHORT).show()
                }
            }
    }
}