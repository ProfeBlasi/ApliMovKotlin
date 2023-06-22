package com.example.tpintegrador.DataBase.Entities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpintegrador.R

class StudentAdapter(private val studentList: MutableList<Student>) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtLastName: TextView = itemView.findViewById(R.id.edtLastNameStudent)
        private val txtName: TextView = itemView.findViewById(R.id.edtNameStudent)

        fun bind(student: Student) {
            txtLastName.text = student.lastName
            txtName.text = student.nameStudent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.bind(student)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun updateStudent(updatedStudents: List<Student>) {
        studentList.clear()
        studentList.addAll(updatedStudents)
        notifyDataSetChanged()
    }
}