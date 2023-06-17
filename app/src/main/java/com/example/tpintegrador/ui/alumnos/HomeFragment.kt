package com.example.tpintegrador.ui.alumnos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tpintegrador.DataBase.Entities.Alumno
import com.example.tpintegrador.DataBase.Entities.AlumnosAdapter
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurar el adaptador para el ListView y otras operaciones del CRUD
        val dbHelper = DBHelper(requireContext())
        val listViewAlumnos = binding.listViewAlumnos
        val alumnosList = mutableListOf<Alumno>()
        val alumnosAdapter = AlumnosAdapter(requireContext(), alumnosList)
        listViewAlumnos.adapter = alumnosAdapter

        // Cargar los alumnos existentes
        cargarAlumnos(dbHelper, alumnosList, alumnosAdapter)

        // Obtén las referencias de los elementos de la interfaz de usuario necesarios para el CRUD
        val editTextApellido = binding.editTextApellido
        val editTextNombre = binding.editTextNombre
        val editTextTelefono = binding.editTextTelefono
        val editTextNacionalidad = binding.editTextNacionalidad
        val editTextFechaNacimiento = binding.editTextFechaNacimiento
        val editTextDNI = binding.editTextDNI
        val editTextEmail = binding.editTextEmail
        val editTextEstado = binding.editTextEstado
        val editTextPromedio = binding.editTextPromedio
        val buttonAgregar = binding.buttonAgregar

        listViewAlumnos.setOnItemClickListener { parent, view, position, id ->
            val alumno = alumnosList[position]
            val intent = Intent(requireContext(), DetalleAlumnoActivity::class.java)
            intent.putExtra("EXTRA_ALUMNO", alumno)
            startActivity(intent)
        }

        // Manejar el clic del botón "Agregar"
        buttonAgregar.setOnClickListener {
            val apellido = editTextApellido.text.toString()
            val nombre = editTextNombre.text.toString()

            if (apellido.isNotEmpty() && nombre.isNotEmpty()) {
                val telefono = editTextTelefono.text.toString()
                val nacionalidad = editTextNacionalidad.text.toString()
                val fechaNacimiento = editTextFechaNacimiento.text.toString()
                val dni = editTextDNI.text.toString()
                val email = editTextEmail.text.toString()
                val estado = editTextEstado.text.toString()
                val promedio = editTextPromedio.text.toString().toLongOrNull()

                val alumno = Alumno(
                    id = 0,
                    apellido = apellido,
                    nombre = nombre,
                    telefono = telefono,
                    nacionalidad = nacionalidad,
                    fechaNacimiento = fechaNacimiento,
                    dni = dni,
                    email = email,
                    estado = estado,
                    promedio = promedio
                )

                val alumnoId = dbHelper.insertAlumno(alumno)
                if (alumnoId > 0) {
                    alumno.id = alumnoId
                    alumnosList.add(alumno)
                    alumnosAdapter.notifyDataSetChanged()
                    limpiarCampos()
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cargarAlumnos(dbHelper: DBHelper, alumnosList: MutableList<Alumno>, alumnosAdapter: AlumnosAdapter) {
        alumnosList.clear()
        alumnosList.addAll(dbHelper.getAllAlumnos())
        alumnosAdapter.notifyDataSetChanged()
    }

    private fun limpiarCampos() {
        binding.editTextApellido.text.clear()
        binding.editTextNombre.text.clear()
        binding.editTextTelefono.text.clear()
        binding.editTextNacionalidad.text.clear()
        binding.editTextFechaNacimiento.text.clear()
        binding.editTextDNI.text.clear()
        binding.editTextEmail.text.clear()
        binding.editTextEstado.text.clear()
        binding.editTextPromedio.text.clear()
    }
}