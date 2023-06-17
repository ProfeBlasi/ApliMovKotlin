package com.example.tpintegrador.DataBase.Entities

import android.os.Parcel
import android.os.Parcelable

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
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt().toLong(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id.toInt())
        parcel.writeString(apellido)
        parcel.writeString(nombre)
        parcel.writeString(telefono)
        parcel.writeString(nacionalidad)
        parcel.writeString(fechaNacimiento)
        parcel.writeString(dni)
        parcel.writeString(email)
        parcel.writeString(estado)
        parcel.writeValue(promedio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Alumno> {
        override fun createFromParcel(parcel: Parcel): Alumno {
            return Alumno(parcel)
        }

        override fun newArray(size: Int): Array<Alumno?> {
            return arrayOfNulls(size)
        }
    }
}