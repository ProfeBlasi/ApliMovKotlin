package com.example.tpintegrador.DataBase.Entities

import android.os.Parcel
import android.os.Parcelable

data class Student(
    var id: Long,
    var lastName: String?,
    var nameStudent: String?,
    var address: String?,
    var phone: String?,
    var nationality: String?,
    var birthdate: String?,
    var document: String?,
    var email: String?,
    var state: String?,
    val courseId: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

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
    ) : this(-1, lastName, nameStudent, address, phone, nationality, birthdate, document, email, state, courseId)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(lastName)
        parcel.writeString(nameStudent)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeString(nationality)
        parcel.writeString(birthdate)
        parcel.writeString(document)
        parcel.writeString(email)
        parcel.writeString(state)
        parcel.writeString(courseId)
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