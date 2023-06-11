package com.example.tpintegrador.Login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tpintegrador.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        val edtMail: TextView = findViewById(R.id.edtEmailCreate)
        val edtPass: TextView = findViewById(R.id.edtPass)
        val edtConfirmPass: TextView = findViewById(R.id.edtConfirmPass)
        val btnCrear: Button = findViewById(R.id.btn_crear_cuenta_usuario)
        btnCrear.setOnClickListener (){
            var pass1 = edtPass.text.toString()
            var pass2 = edtConfirmPass.text.toString()
            if(validateMail(edtMail.text.toString())){
                if(pass1.length>8){
                    if(pass1.equals(pass2)){
                        createAccount(edtMail.text.toString(), edtPass.text.toString())
                    }else{
                        Toast.makeText(baseContext,"las contrasenias no coinciden", Toast.LENGTH_SHORT).show()
                        edtPass.requestFocus()
                    }
                }else{
                    Toast.makeText(baseContext, "la contrasenia debe tener mas de 8 caracteres", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(baseContext, "El formato del mail es incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
        firebaseAuth = Firebase.auth
    }

    private fun validateMail(email: String): Boolean{
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun createAccount(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    Toast.makeText(baseContext, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
                    val act = Intent(this, Login::class.java)
                    startActivity(act)
                }else{
                    Toast.makeText(baseContext, "Algo salio mal, vuelva a intentarlo" + task.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }
}