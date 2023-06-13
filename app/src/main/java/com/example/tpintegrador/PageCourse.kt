package com.example.tpintegrador

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Curso
import com.example.tpintegrador.DataBase.Entities.CursoAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PageCourse : AppCompatActivity() {
    private lateinit var fabAgregarCurso: FloatingActionButton
    private lateinit var layoutCrearCurso: ConstraintLayout
    private lateinit var editTextNombreCurso: EditText
    private lateinit var btnCrearCurso: Button
    private lateinit var listViewCursos: ListView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_course)
        val dbHelper = DBHelper(baseContext)
        fabAgregarCurso = findViewById(R.id.fabAgregarCurso)
        layoutCrearCurso = findViewById(R.id.layoutCrearCurso)
        editTextNombreCurso = findViewById(R.id.editTextNombreCurso)
        btnCrearCurso = findViewById(R.id.btnCrearCurso)
        val cursos = dbHelper.getAllCursos()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCursos)
        val layoutManager = LinearLayoutManager(this)
        val adapter = CursoAdapter(cursos)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        fabAgregarCurso.setOnClickListener {
            // Ocultar el botón flotante y mostrar el formulario de creación
            fabAgregarCurso.visibility = View.GONE
            layoutCrearCurso.visibility = View.VISIBLE
        }

        btnCrearCurso.setOnClickListener {
            val nameCourse = editTextNombreCurso.text.toString().trim()
            if(nameCourse.isNotEmpty()){
                val curso = Curso(nameCourse)
                val cursoId = dbHelper.insertCurso(curso)
                if (cursoId != -1L) {
                    // El curso se ha guardado exitosamente en la base de datos
                    Toast.makeText(this, "Curso guardado correctamente", Toast.LENGTH_SHORT).show()

                    // Restaurar la visibilidad del botón flotante y ocultar el layout de creación del curso
                    // Restablecer el texto del EditText
                    editTextNombreCurso.text.clear()
                } else {
                    // Ocurrió un error al guardar el curso en la base de datos
                    Toast.makeText(this, "Error al guardar el curso", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Debe ingresar un nombre", Toast.LENGTH_SHORT).show()
            }
            fabAgregarCurso.visibility = View.VISIBLE
            layoutCrearCurso.visibility = View.GONE
        }
    }
}
