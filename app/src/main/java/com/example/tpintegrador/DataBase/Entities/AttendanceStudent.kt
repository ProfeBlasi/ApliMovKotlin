package com.example.tpintegrador.DataBase.Entities

data class AttendanceStudent (
    val id: Long,
    val attendanceDay: String,
    val studentId: String,
    val attendanceCourseId: String,
    val attendanceStudentStatus: String,
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