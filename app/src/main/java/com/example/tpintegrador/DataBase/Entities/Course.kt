package com.example.tpintegrador.DataBase.Entities

data class Course(
    val id: Long,
    val name: String,
    val school: String,
    val shift: String,
    val address: String,
    val userId: String
) {
    constructor(
        name: String,
        school: String,
        shift: String,
        address: String,
        userId: String
    ) : this(-1, name, school, shift, address, userId)
}