package com.example.tpintegrador.DataBase.Entities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpintegrador.R

class CourseAdapter(private val courses: MutableList<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private var onLongClickListener: ((Course) -> Unit)? = null
    private var onClickListener: ((Course) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.bind(course)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtCourseName: TextView = itemView.findViewById(R.id.edtNameCourse)
        private val txtCourseSchool: TextView = itemView.findViewById(R.id.edtSchoolCourse)
        private val txtCourseShift: TextView = itemView.findViewById(R.id.edtShiftCourse)
        private val txtCourseAddress: TextView = itemView.findViewById(R.id.edtAddressCourse)

        fun bind(course: Course) {
            txtCourseName.text = course.name
            txtCourseSchool.text = course.school
            txtCourseShift.text = course.shift
            txtCourseAddress.text = course.address

            itemView.setOnLongClickListener {
                onLongClickListener?.invoke(course)
                true
            }

            itemView.setOnClickListener {
                onClickListener?.invoke(course)
            }
        }
    }

    fun updateCourses(updatedCourses: List<Course>) {
        courses.clear()
        courses.addAll(updatedCourses)
        notifyDataSetChanged()
    }

    fun setOnCourseLongClickListener(listener: (Course) -> Unit) {
        onLongClickListener = listener
    }

    fun setOnCourseClickListener(listener: (Course) -> Unit) {
        onClickListener = listener
    }
}
