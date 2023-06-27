package com.example.tpintegrador.ui.asistencia

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.AttendanceStudent
import com.example.tpintegrador.DataBase.Entities.Student
import com.example.tpintegrador.DataBase.Repository.AttendanceStudentRepository
import com.example.tpintegrador.DataBase.Repository.StudentRepository
import com.example.tpintegrador.MainActivity
import com.example.tpintegrador.R
import com.example.tpintegrador.databinding.FragmentGalleryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var textViewDate: TextView
    private lateinit var buttonPreviousDay: FloatingActionButton
    private lateinit var buttonNextDay: FloatingActionButton
    private lateinit var btnReturn: Button
    private lateinit var btnToRegister: Button
    private val calendar: Calendar = Calendar.getInstance()
    private var isSelectingMonths: Boolean = false
    private lateinit var courseId: String
    private lateinit var studentsMap: HashMap<Long, Student>
    private lateinit var studentDtoList: List<StudentDto>
    private lateinit var editedStudentDtoList: List<StudentDto>
    private var existsChanges: Boolean = false
    private var recordsExist: Boolean = false
    private lateinit var attendanceStudentRepository: AttendanceStudentRepository
    private lateinit var studentRepository: StudentRepository
    private lateinit var studentContainer: LinearLayout
    private lateinit var formattedDate: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mainActivity = requireActivity() as MainActivity
        courseId = mainActivity.getCourseId()
        val dbHelper = DBHelper(requireContext().applicationContext)
        studentRepository = StudentRepository(dbHelper)
        studentsMap = courseId?.let { studentRepository.getAllStudentsMap(it) }!!
        studentDtoList = studentsMap.values.map { it.toStudentDto() }
        textViewDate = binding.textViewDate
        buttonPreviousDay = binding.buttonPreviousDay
        buttonNextDay = binding.buttonNextDay
        btnReturn = binding.btnReturn
        btnToRegister = binding.btnToRegister
        studentContainer = binding.studentContainer
        attendanceStudentRepository = AttendanceStudentRepository(dbHelper)
        updateDate()
        getAttendanceStudentsByDay(formattedDate)
        textViewDate.setOnClickListener {
            changeToSelectionByMonth()
        }

        buttonPreviousDay.setOnClickListener {
            if (isSelectingMonths) {
                calendar.add(Calendar.MONTH, -1)
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, -1)
            }
            updateDate()
        }

        buttonNextDay.setOnClickListener {
            if (isSelectingMonths) {
                calendar.add(Calendar.MONTH, 1)
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            updateDate()
        }

        btnToRegister.setOnClickListener {

            if (recordsExist) {
                for (studentDto in editedStudentDtoList) {
                    val attendanceStudent = AttendanceStudent(
                        studentDto.attendanceId,
                        formattedDate,
                        studentDto.id.toString(),
                        courseId,
                        studentDto.attendanceStatus.name,
                        "0"
                    )
                    attendanceStudentRepository.updateAttendanceStudent(attendanceStudent)
                }
                requireActivity().onBackPressed()
            }

            if (existsChanges) {
                for (studentDto in studentDtoList) {
                    val attendanceStudent = AttendanceStudent(
                        formattedDate,
                        studentDto.id.toString(),
                        courseId,
                        studentDto.attendanceStatus.name,
                        "0"
                    )
                    attendanceStudentRepository.insertAttendanceStudent(attendanceStudent)
                }

                requireActivity().onBackPressed()
            }

            if (isSelectingMonths) {
                isSelectingMonths = false
                btnToRegister.text = getString(R.string.strToAccess)
                updateDate()
            } else {
                buttonPreviousDay.isEnabled = false
                buttonNextDay.isEnabled = false
                textViewDate.isEnabled = false
                existsChanges = true
                btnToRegister.text = "Save"

                loadStudentsBtn(studentDtoList)

            }
        }

        btnReturn.setOnClickListener{
            requireActivity().onBackPressed()
        }

        return root
    }
    private fun getButtonColor(attendanceStatus: AttendanceStatus): Int {
        return when (attendanceStatus) {
            AttendanceStatus.NO_INFORMATION -> Color.BLUE
            AttendanceStatus.PRESENT -> Color.GREEN
            AttendanceStatus.ABSENT -> Color.RED
        }
    }

    private fun getUpdatedAttendanceStatus(attendanceStatus: AttendanceStatus): AttendanceStatus {
        return when (attendanceStatus) {
            AttendanceStatus.NO_INFORMATION -> AttendanceStatus.PRESENT
            AttendanceStatus.PRESENT -> AttendanceStatus.ABSENT
            AttendanceStatus.ABSENT -> AttendanceStatus.NO_INFORMATION
        }
    }
    private fun changeToSelectionByMonth() {
        isSelectingMonths = true
        updateDate()
        btnToRegister.text = "Select month"
    }

    private fun updateDate() {
        val dateFormat = if (isSelectingMonths) {
            SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        } else {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        }
        val currentDate: Date = calendar.time
        formattedDate = dateFormat.format(currentDate)
        textViewDate.text = formattedDate
        getAttendanceStudentsByDay(formattedDate)
    }

    private fun getAttendanceStudentsByDay(attendanceDay: String){
        val attendanceStudents = attendanceStudentRepository.getAttendanceStudentsByDay(attendanceDay)
        if (attendanceStudents.isNotEmpty()) {
            btnToRegister.text = "edit"
            recordsExist = true
            val studentDtoList = studentsMap.values.mapNotNull { student ->
                val attendanceStatus = attendanceStudents.find { it.id == student.id }?.attendanceStatus
                    ?: AttendanceStatus.NO_INFORMATION
                val attendanceId = attendanceStudents.find { it.id == student.id }?.attendanceId
                    ?: 0
                val studentDto = student.id?.let { studentId ->
                    val studentFromRepo = studentRepository.getStudentById(studentId)
                    studentFromRepo?.let {
                        StudentDto(
                            id = it.id,
                            lastName = it.lastName ?: "",
                            nameStudent = it.nameStudent ?: "",
                            attendanceStatus = attendanceStatus,
                            attendanceId = attendanceId
                        )
                    }
                }
                studentDto
            }
            editedStudentDtoList = loadStudentsBtn(studentDtoList)
        } else {
            btnToRegister.text = getString(R.string.strToAccess)
            recordsExist = false
            studentContainer.removeAllViews()
        }
    }

    private fun Student.toStudentDto(): StudentDto {
        return StudentDto(
            id = id,
            lastName = lastName ?: "",
            nameStudent = nameStudent ?: "",
            attendanceStatus = AttendanceStatus.PRESENT,
            attendanceId = 0
        )
    }

    private fun loadStudentsBtn(listStudent: List<StudentDto>): List<StudentDto> {
        val scrollView = ScrollView(requireContext())
        scrollView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        val linearLayout = LinearLayout(requireContext())
        linearLayout.orientation = LinearLayout.VERTICAL
        for (studentDto in listStudent) {
            val studentButton = Button(requireContext())
            studentButton.text = "${studentDto.lastName} ${studentDto.nameStudent}"
            studentButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35f)
            studentButton.setTextColor(Color.WHITE)
            studentButton.setBackgroundColor(getButtonColor(studentDto.attendanceStatus))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(5, 10, 5, 10)
            studentButton.layoutParams = params

            studentButton.setOnClickListener {
                val updatedStatus = getUpdatedAttendanceStatus(studentDto.attendanceStatus)
                studentDto.attendanceStatus = updatedStatus
                studentButton.setBackgroundColor(getButtonColor(updatedStatus))
            }

            linearLayout.addView(studentButton)
        }
        scrollView.addView(linearLayout)
        studentContainer.addView(scrollView)
        return listStudent
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
