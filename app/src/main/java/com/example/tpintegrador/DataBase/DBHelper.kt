package com.example.tpintegrador.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "AlumnoDB.db"
        private const val TABLE_NAME = "alumnos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_APELLIDO = "apellido"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_TELEFONO = "telefono"
        private const val COLUMN_NACIONALIDAD = "nacionalidad"
        private const val COLUMN_FECHA_NACIMIENTO = "fecha_nacimiento"
        private const val COLUMN_DNI = "dni"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ESTADO = "estado"
        private const val COLUMN_PROMEDIO = "promedio"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_APELLIDO TEXT NOT NULL,"
                + "$COLUMN_NOMBRE TEXT NOT NULL,"
                + "$COLUMN_TELEFONO TEXT,"
                + "$COLUMN_NACIONALIDAD TEXT,"
                + "$COLUMN_FECHA_NACIMIENTO TEXT,"
                + "$COLUMN_DNI TEXT,"
                + "$COLUMN_EMAIL TEXT,"
                + "$COLUMN_ESTADO TEXT,"
                + "$COLUMN_PROMEDIO INTEGER);")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Operaciones CRUD

    fun insertAlumno(alumno: Alumno): Long {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_APELLIDO, alumno.apellido)
        contentValues.put(COLUMN_NOMBRE, alumno.nombre)
        contentValues.put(COLUMN_TELEFONO, alumno.telefono)
        contentValues.put(COLUMN_NACIONALIDAD, alumno.nacionalidad)
        contentValues.put(COLUMN_FECHA_NACIMIENTO, alumno.fechaNacimiento)
        contentValues.put(COLUMN_DNI, alumno.dni)
        contentValues.put(COLUMN_EMAIL, alumno.email)
        contentValues.put(COLUMN_ESTADO, alumno.estado)
        contentValues.put(COLUMN_PROMEDIO, alumno.promedio)

        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun updateAlumno(alumno: Alumno): Int {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_APELLIDO, alumno.apellido)
        contentValues.put(COLUMN_NOMBRE, alumno.nombre)
        contentValues.put(COLUMN_TELEFONO, alumno.telefono)
        contentValues.put(COLUMN_NACIONALIDAD, alumno.nacionalidad)
        contentValues.put(COLUMN_FECHA_NACIMIENTO, alumno.fechaNacimiento)
        contentValues.put(COLUMN_DNI, alumno.dni)
        contentValues.put(COLUMN_EMAIL, alumno.email)
        contentValues.put(COLUMN_ESTADO, alumno.estado)
        contentValues.put(COLUMN_PROMEDIO, alumno.promedio)

        return db.update(
            TABLE_NAME, contentValues, "$COLUMN_ID = ?",
            arrayOf(alumno.id.toString())
        )
    }

    fun deleteAlumno(alumnoId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(alumnoId.toString()))
    }

    @SuppressLint("Range")
    fun getAllAlumnos(): List<Alumno> {
        val alumnos = mutableListOf<Alumno>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val apellido = cursor.getString(cursor.getColumnIndex(COLUMN_APELLIDO))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
                val telefono = cursor.getString(cursor.getColumnIndex(COLUMN_TELEFONO))
                val nacionalidad = cursor.getString(cursor.getColumnIndex(COLUMN_NACIONALIDAD))
                val fechaNacimiento = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_NACIMIENTO))
                val dni = cursor.getString(cursor.getColumnIndex(COLUMN_DNI))
                val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
                val estado = cursor.getString(cursor.getColumnIndex(COLUMN_ESTADO))
                val promedio = cursor.getLong(cursor.getColumnIndex(COLUMN_PROMEDIO))

                val alumno = Alumno(
                    id,
                    apellido,
                    nombre,
                    telefono,
                    nacionalidad,
                    fechaNacimiento,
                    dni,
                    email,
                    estado,
                    promedio
                )
                alumnos.add(alumno)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return alumnos
    }
}