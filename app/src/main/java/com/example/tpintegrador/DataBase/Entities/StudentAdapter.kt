package com.example.tpintegrador.DataBase.Entities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tpintegrador.R

class StudentAdapter(
    private val context: Context,
    private val alumnosList: List<Student>
) : ArrayAdapter<Student>(context, 0, alumnosList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.alumno_item, parent, false)
        }

        val alumno = alumnosList[position]

        val textViewNombre = itemView?.findViewById<TextView>(R.id.textViewNombreAlumno)
        textViewNombre?.text = "${alumno.lastName}, ${alumno.name}"

        val textViewDni = itemView?.findViewById<TextView>(R.id.textViewDNI)
        textViewDni?.text = alumno.document

        val textViewEmail = itemView?.findViewById<TextView>(R.id.textViewEmail)
        textViewEmail?.text = alumno.email

        return itemView!!
    }
}