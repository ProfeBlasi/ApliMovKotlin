package com.example.tpintegrador.DataBase.Entities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpintegrador.R

class StudentAdapter(private val studentsMap: HashMap<Long, Student>) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    private var itemClickListener: ((Student) -> Unit)? = null
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
        val student = studentsMap.values.elementAt(position)
        holder.bind(student)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(student)
        }
    }

    override fun getItemCount(): Int {
        return studentsMap.size
    }

    fun setOnItemClickListener(listener: (Student) -> Unit) {
        itemClickListener = listener
    }
    fun updateStudents(updatedStudents: HashMap<Long, Student>) {
        studentsMap.clear()
        studentsMap.putAll(updatedStudents)
        notifyDataSetChanged()
    }

    fun updateData(updatedStudents: HashMap<Long, Student>) {
        studentsMap.clear()
        studentsMap.putAll(updatedStudents)
        notifyDataSetChanged()
    }

}
