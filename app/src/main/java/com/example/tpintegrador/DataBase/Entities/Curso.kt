package com.example.tpintegrador.DataBase.Entities

data class Curso(val id: Long, val nombre: String){
    constructor(nombre: String) : this(-1, nombre)
}