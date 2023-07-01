package com.example.tpintegrador.ui.students

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Student
import com.example.tpintegrador.DataBase.Repository.StudentRepository
import com.example.tpintegrador.MainActivity
import com.example.tpintegrador.R

class StudentShowViewModel : AppCompatActivity() {
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
    private lateinit var btnCam: Button
    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_REQUEST_CODE = 200
    private lateinit var imgPhoto: ImageView
    private lateinit var studentRepository: StudentRepository
    private lateinit var student: Student


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_student)
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
        btnCam = findViewById(R.id.btnCam)
        imgPhoto = findViewById(R.id.imgPhoto)
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

        btnCam.setOnClickListener {
            dispatchTakePictureIntent()
        }

        btnReturn.setOnClickListener{
            returnStudent()
        }
    }

    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
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
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "No se encontró ninguna aplicación de cámara.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap
            imgPhoto.setImageBitmap(imageBitmap)
        }
    }
}