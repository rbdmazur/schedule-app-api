package com.example.testdata.ui

import com.example.repository.model.Subject
import com.example.repository.model.Teacher
import com.example.testdata.facultyRepository
import com.example.testdata.scheduleRepository
import com.example.testdata.usersRepository
import com.example.utils.Constants.TIMES
import com.example.utils.DaysOfWeek
import com.example.utils.TypeOfStudy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.*

class AddStudyKotlin {
    companion object {
        fun createGUI() {
            val frame = JFrame()
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            val panel = JPanel()
            val button = JButton("Add Study Kotlin")

            panel.add(button)
            frame.contentPane.add(panel)

            frame.isVisible = true
            frame.setSize(400, 400)

            button.addActionListener {
                createAddScheduleFrame()
            }
        }

        private val courses = arrayOf(1, 2, 3, 4)
        private val groups = arrayOf(1, 2, 3, 4, 5)
        private val faculties = arrayOf("ФПМИ", "Истфак")

        private fun createAddScheduleFrame() {
            val frame = JFrame("Add schedule")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            val mainPanel = JPanel()
            val layout = FlowLayout(FlowLayout.CENTER)
            mainPanel.layout = layout

            val chooserPanel = JPanel()
            chooserPanel.layout = FlowLayout(FlowLayout.CENTER)

            val facultyComboBox = JComboBox(faculties)
            val courseComboBox = JComboBox(courses)
            val groupComboBox = JComboBox(groups)

            chooserPanel.add(facultyComboBox)
            chooserPanel.add(courseComboBox)
            chooserPanel.add(groupComboBox)

            mainPanel.add(chooserPanel)

            val titlePanel = JPanel()
            titlePanel.layout = FlowLayout(FlowLayout.CENTER)
            val textField = JTextField(25)
            val label = JLabel("Title")
            val button = JButton("Add")
            titlePanel.add(label)
            titlePanel.add(textField)
            titlePanel.add(button)

            mainPanel.add(titlePanel)

            frame.contentPane.add(mainPanel)
            frame.isVisible = true
            frame.setSize(400, 400)

            button.addActionListener {
                val title = textField.text
                val course = courseComboBox.selectedItem as Int
                val group = groupComboBox.selectedItem as Int
                val faculty = facultyComboBox.selectedItem as String

                CoroutineScope(Dispatchers.Default).launch {
                    val facultyId = facultyRepository.getFacultyByName(faculty)!!.id
                    val infoId = facultyRepository.getInfoByFacultyCourseGroup(facultyId, course, group)!!.id

                    val scheduleId = scheduleRepository.addSchedule(title, infoId).id
                    val subjects = scheduleRepository.getSubjectsByInfo(infoId)
                    val teachers = usersRepository.getTeachersByFaculty(facultyId)

                    createStudies(scheduleId = scheduleId, subjects, teachers)
                }

                frame.isVisible = false
            }
        }

        private fun createStudies(scheduleId: Int, subjects: List<Subject>, teachers: List<Teacher>) {
            val frame = JFrame("Studies")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            val mainPanel = JPanel()

            val studiesAddingPanel = JPanel()
            studiesAddingPanel.layout = GridLayout(7, 1, 0, 8)
            val days = DaysOfWeek.entries.iterator()
            var currentDay = days.next()

            var count = 0

            val daysLabel = JLabel("Day: ${currentDay}")
            val daysPanel = JPanel()
            daysPanel.layout = FlowLayout(FlowLayout.CENTER)
            daysPanel.add(daysLabel)
            studiesAddingPanel.add(daysPanel)

            val subjectLabel = JLabel("Subject: ")
            val subjectComboBox = JComboBox(subjects.map { it.title }.toTypedArray())
            val subjectPanel = JPanel()
            subjectPanel.layout = FlowLayout(FlowLayout.CENTER)
            subjectPanel.add(subjectLabel)
            subjectPanel.add(subjectComboBox)
            studiesAddingPanel.add(subjectPanel)

            val timeLabel = JLabel("Time: ")
            val timeComboBox = JComboBox(TIMES)
            val timePanel = JPanel()
            timePanel.layout = FlowLayout(FlowLayout.CENTER)
            timePanel.add(timeLabel)
            timePanel.add(timeComboBox)
            studiesAddingPanel.add(timePanel)

            val typeLabel = JLabel("Type: ")
            val typeComboBox = JComboBox(TypeOfStudy.entries.toTypedArray())
            val typePanel = JPanel()
            typePanel.layout = FlowLayout(FlowLayout.CENTER)
            typePanel.add(typeLabel)
            typePanel.add(typeComboBox)
            studiesAddingPanel.add(typePanel)

            val auditoriumLabel = JLabel("Auditorium: ")
            val auditoriumTextField = JTextField(7)
            val auditoriumPanel = JPanel()
            auditoriumPanel.layout = FlowLayout(FlowLayout.CENTER)
            auditoriumPanel.add(auditoriumLabel)
            auditoriumPanel.add(auditoriumTextField)
            studiesAddingPanel.add(auditoriumPanel)

            val teachersLabel = JLabel("Teachers: ")
            val teachersComboBox = JComboBox(teachers.map { it.name }.toTypedArray())
            val teachersPanel = JPanel()
            teachersPanel.layout = FlowLayout(FlowLayout.CENTER)
            teachersPanel.add(teachersLabel)
            teachersPanel.add(teachersComboBox)
            studiesAddingPanel.add(teachersPanel)

            val addStudyButton = JButton("Add Study")
            val nextButton = JButton("Next")
            val buttonPanel = JPanel()
            buttonPanel.layout = FlowLayout(FlowLayout.RIGHT)
            buttonPanel.add(addStudyButton)
            buttonPanel.add(nextButton)
            studiesAddingPanel.add(buttonPanel)

            addStudyButton.addActionListener {
                CoroutineScope(Dispatchers.Default).launch {
                    val day = currentDay
                    val subject = subjects.find { it.title == subjectComboBox.selectedItem as String }!!
                    val time = timeComboBox.selectedItem as String
                    val auditorium = auditoriumTextField.text
                    val type = typeComboBox.selectedIndex
                    val teacher = teachers.find { it.name == teachersComboBox.selectedItem as String }!!

                    scheduleRepository.addStudy(
                        subject = subject,
                        day = day,
                        time = time,
                        auditorium = auditorium,
                        teacher = teacher,
                        type = TypeOfStudy.entries[type],
                        number = count,
                        scheduleId = scheduleId,
                    )
                }
                count++
            }

            nextButton.addActionListener {
                if (days.hasNext()) {
                    count = 0
                    currentDay = days.next()
                    daysLabel.text = "Day: ${currentDay}"
                } else {
                    frame.isVisible = false
                }
            }

            mainPanel.add(studiesAddingPanel)
            frame.add(mainPanel)
            frame.isVisible = true
            frame.setSize(400, 400)
        }
    }
}