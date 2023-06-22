package com.example.tpintegrador.ui.student

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Student
import com.example.tpintegrador.DataBase.Repository.CourseRepository
import com.example.tpintegrador.DataBase.Repository.StudentRepository
import com.example.tpintegrador.MainActivity
import com.example.tpintegrador.PageCourse
import com.example.tpintegrador.R

class HomeViewModel : AppCompatActivity() {
    private lateinit var txtLastName: TextView
    private lateinit var txtName: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtNationality: TextView
    private lateinit var txtBirthDate: TextView
    private lateinit var txtDocument: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtState: TextView
    private lateinit var txtAddress: TextView
    private lateinit var edtLastName: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtNationality: EditText
    private lateinit var edtBirthdate: EditText
    private lateinit var edtDocument: EditText
    private lateinit var edtEmailStudent: EditText
    private lateinit var edtState: EditText
    private lateinit var edtAddress: EditText
    private lateinit var btnUpdateStudent: Button
    private lateinit var btnDelete: Button
    private lateinit var btnReturn: Button
    private lateinit var studentRepository: StudentRepository
    private lateinit var student: Student

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_alumno)
        val dbHelper = DBHelper(baseContext)
        studentRepository = StudentRepository(dbHelper)
        txtLastName = findViewById(R.id.txtLastName)
        txtName = findViewById(R.id.txtName)
        txtPhone = findViewById(R.id.txtPhone)
        txtNationality = findViewById(R.id.txtNationality)
        txtBirthDate = findViewById(R.id.txtBirthDate)
        txtDocument = findViewById(R.id.txtDocument)
        txtEmail = findViewById(R.id.txtEmail)
        txtState = findViewById(R.id.txtState)
        txtAddress = findViewById(R.id.txtAddress)
        edtLastName = findViewById(R.id.edtLastName)
        edtName = findViewById(R.id.edtName)
        edtPhone = findViewById(R.id.edtPhone)
        edtNationality = findViewById(R.id.edtNationality)
        edtBirthdate = findViewById(R.id.edtBirthdate)
        edtDocument = findViewById(R.id.edtDocument)
        edtEmailStudent = findViewById(R.id.edtEmailStudent)
        edtState = findViewById(R.id.edtState)
        edtAddress = findViewById(R.id.edtAddress)
        btnUpdateStudent = findViewById(R.id.btnUpdateStudent)
        btnDelete = findViewById(R.id.btnDelete)
        btnReturn = findViewById(R.id.btnReturn)
        student = intent.getParcelableExtra("EXTRA_STUDENT")!!

        if (student != null) {
            showStudentDetails(student)
        }

        btnUpdateStudent.setOnClickListener {
            if (student != null) {
                val updatedLastName = edtLastName.text.toString()
                val updatedName = edtName.text.toString()
                val updatedAddress = edtAddress.text.toString()
                val updatedPhone = edtPhone.text.toString()
                val updatedNationality = edtNationality.text.toString()
                val updatedBirthdate = edtBirthdate.text.toString()
                val updatedDocument = edtDocument.text.toString()
                val updatedEmail = edtEmailStudent.text.toString()
                val updatedState = edtState.text.toString()
                student!!.lastName = updatedLastName
                student!!.nameStudent = updatedName
                student!!.address = updatedAddress
                student!!.phone = updatedPhone
                student!!.nationality = updatedNationality
                student!!.birthdate = updatedBirthdate
                student!!.document = updatedDocument
                student!!.email = updatedEmail
                student!!.state = updatedState
                studentRepository.updateStudent(student!!)
            }
            returnStudent()
        }

        btnDelete.setOnClickListener {
            student?.let { showDialogConfirmationDelete(it) }
        }

        btnReturn.setOnClickListener{
            returnStudent()
        }
    }

    private fun showStudentDetails(student: Student?) {
        edtLastName.setText(student?.lastName)
        edtName.setText(student?.nameStudent)
        edtAddress.setText(student?.address)
        edtPhone.setText(student?.phone)
        edtNationality.setText(student?.nationality)
        edtBirthdate.setText(student?.birthdate)
        edtDocument.setText(student?.document)
        edtEmailStudent.setText(student?.email)
        edtState.setText(student?.state)
    }

    private fun showDialogConfirmationDelete(student: Student) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.deleteStudent))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
               studentRepository.deleteStudent(student.id)
                returnStudent()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun returnStudent(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(PageCourse.COURSE_ID, student?.courseId)
        startActivity(intent)
    }
}