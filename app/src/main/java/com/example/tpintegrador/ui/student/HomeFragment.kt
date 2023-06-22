package com.example.tpintegrador.ui.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Student
import com.example.tpintegrador.DataBase.Entities.StudentAdapter
import com.example.tpintegrador.DataBase.Repository.StudentRepository
import com.example.tpintegrador.PageCourse.Companion.COURSE_ID
import com.example.tpintegrador.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DBHelper
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var edtNameStudent: EditText
    private lateinit var edtLastNameStudent:EditText
    private lateinit var fabAddStudent: FloatingActionButton
    private lateinit var layoutCreateStudent: ConstraintLayout
    private lateinit var recyclerViewStudent: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fabAddStudent = binding.fabAddStudent
        layoutCreateStudent = binding.layoutCreateStudent
        edtNameStudent = binding.edtNameStudent
        edtLastNameStudent = binding.edtLastNameStudent
        val btnCreateStudent = binding.btnCreateStudent
        val btnCancelCreateStudent = binding.btnCancelCreateStudent
        recyclerViewStudent = binding.recyclerViewStudent
        val courseId: String? = arguments?.getString(COURSE_ID)
        dbHelper = DBHelper(requireContext().applicationContext)
        val studentRepository = StudentRepository(dbHelper)

        fabAddStudent.setOnClickListener {
            fabAddStudent.visibility = View.GONE
            layoutCreateStudent.visibility = View.VISIBLE
            recyclerViewStudent.visibility = View.GONE
        }

        btnCancelCreateStudent.setOnClickListener {
            clearInputFields()
            showStudentList()
        }

        btnCreateStudent.setOnClickListener {
            val lastNameStudent = edtNameStudent.text.toString().trim()
            val nameStudent = edtLastNameStudent.text.toString().trim()
            if (lastNameStudent.isNotEmpty() && nameStudent.isNotEmpty()) {
                val student = courseId?.let { it1 ->
                    Student(lastNameStudent,nameStudent,"","","","","","","",
                        it1
                    )
                }
                val studentId = student?.let { it1 -> studentRepository.insertStudent(it1) }
                if (studentId == -1L) {
                    Toast.makeText(requireContext().applicationContext, "Error adding student", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext().applicationContext, "Please enter the student's first and last name", Toast.LENGTH_SHORT).show()
            }
            clearInputFields()
            showStudentList()
            val studentList = courseId?.let { it1 -> studentRepository.getAllStudents(it1) }
            if (studentList != null) {
                studentAdapter.updateStudent(studentList)
            }
        }

        val studentList = courseId?.let { studentRepository.getAllStudents(it)?.toMutableList() } ?: mutableListOf()
        studentAdapter = StudentAdapter(studentList)
        recyclerViewStudent.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewStudent.adapter = studentAdapter

        return root
    }
    private fun clearInputFields() {
        edtNameStudent.text.clear()
        edtLastNameStudent.text.clear()
    }
    private fun showStudentList() {
        fabAddStudent.visibility = View.VISIBLE
        layoutCreateStudent.visibility = View.GONE
        recyclerViewStudent.visibility = View.VISIBLE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}