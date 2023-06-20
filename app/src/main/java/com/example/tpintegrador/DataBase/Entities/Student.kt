package com.example.tpintegrador.DataBase.Entities

import android.os.Parcel
import android.os.Parcelable

data class Student(
    var id: Long,
    val lastName: String,
    val name: String,
    val address: String?,
    val phone: String?,
    val nationality: String?,
    val birthdate: String?,
    val document: String?,
    val email: String?,
    val state: String?,
    val courseId: String?
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
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id.toInt())
        parcel.writeString(lastName)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeString(nationality)
        parcel.writeString(birthdate)
        parcel.writeString(document)
        parcel.writeString(email)
        parcel.writeString(state)
        parcel.writeValue(courseId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}