package com.example.tpintegrador.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tpintegrador.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RememberPassword : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remember_password)
        val edtMail : TextView = findViewById(R.id.edtEmailRememberPass)
        val btnChanged : Button = findViewById(R.id.btnRemembarPass)
        btnChanged.setOnClickListener(){
            sendPasswordReset(edtMail.text.toString())
        }
        firebaseAuth = Firebase.auth
    }

    private fun sendPasswordReset(email: String){
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(){task ->
                if(task.isSuccessful){
                    Toast.makeText(baseContext, "Revisa tu casilla de mail para finalizar el proceso", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(baseContext, "ERROR vuelva a intentarlo mas tarde", Toast.LENGTH_SHORT).show()
                }
            }
    }
}