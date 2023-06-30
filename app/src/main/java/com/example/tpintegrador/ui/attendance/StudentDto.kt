package com.example.tpintegrador.ui.attendance

data class StudentDto(
    var id: Long,
    var idAttendance: Long = 0,
    var lastName: String,
    var nameStudent: String,
    var attendanceStatus: AttendanceStatus = AttendanceStatus.PRESENT,
    val attendanceId: Long,
)