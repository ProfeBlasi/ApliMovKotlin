package com.example.tpintegrador.DataBase

data class Alumno(
    var id: Long,
    val apellido: String,
    val nombre: String,
    val telefono: String?,
    val nacionalidad: String?,
    val fechaNacimiento: String?,
    val dni: String?,
    val email: String?,
    val estado: String?,
    val promedio: Long?
)