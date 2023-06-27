package com.example.tpintegrador.DataBase.Entities

import java.util.Date

data class AttendanceStudent (
    val id: Long,
    val attendanceDay: String,
    val studentId: String,
    val courseId: String,
    val attendanceStatus: String,
    val referenceId: String
) {
    constructor(
        attendanceDay: String,
        studentId: String,
        courseId: String,
        attendanceStatus: String,
        referenceId: String
    ) : this(-1,attendanceDay,studentId,courseId,attendanceStatus,referenceId)
}