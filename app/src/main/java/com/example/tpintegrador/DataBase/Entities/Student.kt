package com.example.tpintegrador.DataBase.Entities

data class Student(
    var id: Long,
    val lastName: String,
    val nameStudent: String,
    val address: String,
    val phone: String,
    val nationality: String,
    val birthdate: String,
    val document: String,
    val email: String,
    val state: String,
    val courseId: String
){
    constructor(
        lastName: String,
        nameStudent: String,
        address: String,
        phone: String,
        nationality: String,
        birthdate: String,
        document: String,
        email: String,
        state: String,
        courseId: String
    ) : this(-1,lastName,nameStudent,address,phone,nationality,birthdate,document,email,state,courseId)
}