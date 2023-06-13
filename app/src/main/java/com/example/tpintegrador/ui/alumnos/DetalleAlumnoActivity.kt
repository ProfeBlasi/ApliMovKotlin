package com.example.tpintegrador.ui.alumnos

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.tpintegrador.DataBase.Alumno
import com.example.tpintegrador.R

class DetalleAlumnoActivity : AppCompatActivity() {
    private lateinit var textViewApellido: TextView
    private lateinit var textViewNombre: TextView
    private lateinit var textViewTelefono: TextView
    private lateinit var textViewNacionalidad: TextView
    private lateinit var textViewFechaNacimiento: TextView
    private lateinit var textViewDNI: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewEstado: TextView
    private lateinit var textViewPromedio: TextView
    private lateinit var editTextApellido: EditText
    private lateinit var editTextNombre: EditText
    private lateinit var editTextTelefono: EditText
    private lateinit var editTextNacionalidad: EditText
    private lateinit var editTextFechaNacimiento: EditText
    private lateinit var editTextDNI: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextEstado: EditText
    private lateinit var editTextPromedio: EditText
    private lateinit var buttonEditar: Button
    private lateinit var buttonBorrar: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_alumno)

        textViewApellido = findViewById(R.id.textViewApellido)
        textViewNombre = findViewById(R.id.textViewNombre)
        textViewTelefono = findViewById(R.id.textViewTelefono)
        textViewNacionalidad = findViewById(R.id.textViewNacionalidad)
        textViewFechaNacimiento = findViewById(R.id.textViewFechaNacimiento)
        textViewDNI = findViewById(R.id.textViewDNI)
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewEstado = findViewById(R.id.textViewEstado)
        textViewPromedio = findViewById(R.id.textViewPromedio)
        editTextApellido = findViewById(R.id.editTextApellido)
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextTelefono = findViewById(R.id.editTextTelefono)
        editTextNacionalidad = findViewById(R.id.editTextNacionalidad)
        editTextFechaNacimiento = findViewById(R.id.editTextFechaNacimiento)
        editTextDNI = findViewById(R.id.editTextDNI)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextEstado = findViewById(R.id.editTextEstado)
        editTextPromedio = findViewById(R.id.editTextPromedio)
        buttonEditar = findViewById(R.id.buttonEditar)
        buttonBorrar = findViewById(R.id.buttonBorrar)

        val alumno: Alumno? = intent.getParcelableExtra("EXTRA_ALUMNO")

        if (alumno != null) {
            mostrarDetallesAlumno(alumno)
        }

        buttonEditar.setOnClickListener {

        }

        buttonBorrar.setOnClickListener {
            // Mostrar diálogo de confirmación para borrar el alumno
            if (alumno != null) {
                mostrarDialogoConfirmacionBorrar(alumno)
            }
        }
    }

    private fun mostrarDetallesAlumno(alumno: Alumno?) {
        editTextApellido.setText(alumno?.apellido)
        editTextNombre.setText(alumno?.nombre)
        editTextTelefono.setText(alumno?.telefono)
        editTextNacionalidad.setText(alumno?.nacionalidad)
        editTextFechaNacimiento.setText(alumno?.fechaNacimiento)
        editTextDNI.setText(alumno?.dni)
        editTextEmail.setText(alumno?.email)
        editTextEstado.setText(alumno?.estado)
        editTextPromedio.setText(alumno?.promedio?.toString() ?: "")
    }

    private fun mostrarDialogoConfirmacionBorrar(alumno: Alumno) {
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Estás seguro de que deseas borrar este alumno?")
            .setPositiveButton("Borrar") { _, _ ->
                // Realizar la acción de borrado
                // ...
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}