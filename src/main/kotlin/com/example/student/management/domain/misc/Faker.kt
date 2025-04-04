package com.example.student.management.domain.misc

import com.example.student.management.domain.dto.CourseDTO
import com.example.student.management.domain.dto.StudentDTO
import com.example.student.management.domain.entities.Gender
import java.io.FileReader
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.ranges.random

class Mobile(private val prefix: String = "+1") {

    fun mobile(): String {
        // +44-962-697-3204
        return StringBuilder().apply {
            this.append("$prefix-")
            repeat(3) { this.append((1..9).random()) }
            this.append("-")
            repeat(3) { this.append((0..9).random()) }
            this.append("-")
            repeat(4) { this.append((0..9).random()) }
        }.toString()
    }

    fun landLine(): String {
        // +44-28-34-3204
        return StringBuilder().apply {
            this.append("$prefix-")
            repeat(2) { this.append((1..9).random()) }
            this.append("-")
            repeat(2) { this.append((0..9).random()) }
            this.append("-")
            repeat(4) { this.append((0..9).random()) }
        }.toString()
    }
}

class Name(fakerDir: String = "/Users/mac/Documents/Faker", private val highestAge: Int, private val lowestAge: Int) {
    private var canUse = false
    private var currentFn: String? = null
    private var currentLn: String? = null
    private var currentAge: Int? = null
    private var currentGender: Gender = Gender.OTHER

    fun getAge(ageInSeconds: Int): LocalDateTime {
        return Instant.ofEpochSecond(ageInSeconds.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    private val _dateOfBirth: LocalDateTime
        get() = getAge(currentAge!! * 31_557_600)

    init {
        currentGender = Gender.entries.random()
        if (firstNames.first.isEmpty() || firstNames.second.isEmpty() || lastNames.isEmpty()) {
            try {
                val males = mutableListOf<String>()
                val females = mutableListOf<String>()
                FileReader("$fakerDir/first_names.txt").readLines().forEach { line ->
                    var male = true
                    line.split(" ").forEach {
                        if (male) males.add(it.lowercase().replaceFirstChar { it.uppercase() })
                        else females.add(it.lowercase().replaceFirstChar { it.uppercase() })
                        male = !male
                    }
                }
                firstNames = males to females

                val ln = mutableListOf<String>()
                FileReader("$fakerDir/last_names.txt").readLines().forEach { line ->
                    line.split(" ").forEach { ln.add(it.lowercase().replaceFirstChar { it.uppercase() }) }
                }
                lastNames = ln
                canUse = true
            } catch (ex: Exception) { println(ex) }
        }
    }


    fun age(): Int = currentAge ?: (lowestAge..highestAge).random().also { currentAge = it }
    fun dateOfBirth(): LocalDateTime = age().let { _dateOfBirth }
    fun gender(): Gender = currentGender
    fun firstName(gender: Gender? = null): String {
        if (gender != null) currentGender = gender
        if (gender == Gender.OTHER) {
            return currentFn ?: (firstNames.first + firstNames.second).random().also { currentFn = it }
        }
        if (gender == Gender.MALE) {
            return currentFn ?: firstNames.first.random().also { currentFn = it }
        }
        return currentFn ?: firstNames.second.random().also { currentFn = it }
    }
    fun lastName(): String = currentLn ?: lastNames.random().also { currentLn = it }
}

class Email(private val firstName: String, private val lastName: String, private val age: Int) {

    private val emailDomains = listOf("gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "aol.com",
        "icloud.com", "protonmail.com", "mail.com", "yandex.ru", "zoho.com", "gmx.com", "live.com",
        "qq.com", "mail.ru", "tutanota.com", "inbox.com", "fastmail.com", "me.com", "msn.com", "rocketmail.com"
    )

    fun emailWithFirstName(): String = firstName + firstName.take(3) + "@" + emailDomains.random()
    fun emailWithFirstNameAndAge(): String = firstName + age + "@" + emailDomains.random()
    fun emailWithLastName(): String = lastName + lastName.take(3) + "@" + emailDomains.random()
    fun emailWithLastNameAndAge(): String = lastName + age + "@" + emailDomains.random()
    fun emailWithBothName(): String = lastName + lastName + "@" + emailDomains.random()
    fun emailWithBothNameAndAge(): String = firstName + lastName + age + "@" + emailDomains.random()
}

var firstNames = Pair<List<String>, List<String>>(emptyList(), emptyList())
var lastNames = emptyList<String>()
class Student(private val highestAge: Int, private val lowestAge: Int) {
    private var nameBuilder = Name(highestAge = highestAge, lowestAge = lowestAge)
    private lateinit var emailBuilder: Email
    private lateinit var mobileBuilder: Mobile
    private var departmentPrograms: Pair<String, String>? = null

    private val _departmentPrograms = mapOf(
        "Science" to listOf(
            "Advanced Placement (AP) Programs",
            "STEM Programs (Science, Technology, Engineering, and Math)",
            "Vocational and Technical Education Programs"
        ),
        "Fine Arts" to listOf("Sculpture", "Gifted and Talented Programs", "Painting"),
        "Social Studies" to listOf("International Baccalaureate (IB) Program", "Gifted and Talented Programs", "Government Management"),
        "Literature" to listOf("World Literature", "Writing during Renaissance", "Language History"),
        "Mathematics" to listOf(
            "Advanced Placement (AP) Programs", "STEM Programs (Science, Technology, Engineering, and Math)", "Honors Programs"
        ),
        "History" to listOf(
            "Special Education Programs", "Dual Enrollment (College Courses for High School Students)"
        ),
        "Computer Science" to listOf(
            "Vocational and Technical Education Programs", "Dual Enrollment (College Courses for High School Students)"
        ),
        "Physical Education" to listOf(
            "Health Education", "Sports Programs"
        )
    )


    fun name(): Name = nameBuilder
    fun configureName(fakerDir: String = "/Users/mac/Documents/Faker", highestAge: Int = 25, lowestAge: Int = 18): Student {
        nameBuilder = Name(fakerDir, highestAge, lowestAge)
        return this
    }

    fun email(): Email {
        val firstName = name().firstName().lowercase()
        val lastName = name().lastName().lowercase()
        val age = name().age()
        emailBuilder = Email(firstName, lastName, age)
        return emailBuilder
    }

    fun mobile(): Mobile {
        mobileBuilder = Mobile()
        return mobileBuilder
    }

    fun program(): String {
        return if (departmentPrograms != null) departmentPrograms!!.second
        else {
            val newDepartmentAndPrograms = _departmentPrograms.entries.random()
            departmentPrograms = newDepartmentAndPrograms.key to newDepartmentAndPrograms.key
            departmentPrograms!!.second
        }
    }

    fun department(): String {
        return if (departmentPrograms != null) departmentPrograms!!.first
        else {
            val newDepartmentAndPrograms = _departmentPrograms.entries.random()
            departmentPrograms = newDepartmentAndPrograms.key to newDepartmentAndPrograms.key
            departmentPrograms!!.first
        }
    }
}

val courses = mutableListOf<CourseDTO>()
class Course {

    val departmentCourses = mapOf(
        "Science" to mapOf(
            "Physics" to "Study of matter, energy, and the interactions between them",
            "Chemistry" to "Study of substances, their properties, and reactions",
            "Biology" to "Study of living organisms and their interactions with the environment",
            "Astronomy" to "Study of celestial objects, space, and the universe as a whole",
            "Environmental Science" to "Study of the environment and the solutions to environmental problems"
        ),
        "Mathematics" to mapOf(
            "Algebra" to "Study of mathematical symbols and the rules for manipulating them",
            "Calculus" to "Study of change in the form of limits, derivatives, and integrals",
            "Geometry" to "Study of shapes, sizes, and the properties of space",
            "Statistics" to "Study of data collection, analysis, interpretation, and presentation",
            "Probability" to "Study of random events and the likelihood of outcomes"
        ),
        "Literature" to mapOf(
            "English Literature" to "Study of written works, including novels, plays, and poetry",
            "World Literature" to "Study of literary works from various cultures and regions",
            "Poetry" to "Study of written or spoken verse that expresses ideas and emotions",
            "Shakespearean Literature" to "Study of works by William Shakespeare and his impact on literature",
            "Modern Literature" to "Study of literary works from the late 19th century to the present"
        ),
        "History" to mapOf(
            "World History" to "Study of global historical events and civilizations",
            "Modern History" to "Study of events from the Renaissance to the present",
            "Ancient Civilizations" to "Study of early human history and ancient cultures",
            "American History" to "Study of the history of the United States from its founding to the present",
            "European History" to "Study of European civilizations, wars, and movements throughout history"
        ),
        "Computer Science" to mapOf(
            "Programming" to "Study of writing code and software development",
            "Data Structures" to "Study of organizing and storing data efficiently",
            "Algorithms" to "Study of step-by-step procedures for solving problems",
            "Artificial Intelligence" to "Study of creating machines that can perform tasks that require human intelligence",
            "Cybersecurity" to "Study of protecting systems and networks from cyber threats"
        ),
        "Social Studies" to mapOf(
            "Geography" to "Study of the Earth's physical features and the relationships between people and their environments",
            "Civics" to "Study of the rights and duties of citizenship and the functioning of government",
            "Economics" to "Study of the production, distribution, and consumption of goods and services",
            "Psychology" to "Study of the human mind and behavior",
            "Sociology" to "Study of society, social institutions, and human relationships"
        ),
        "Fine Arts" to mapOf(
            "Painting" to "Study of visual art through the use of paint and other media",
            "Sculpture" to "Study of three-dimensional art created by shaping materials",
            "Music" to "Study of sound, rhythm, and melody, including performance and theory",
            "Theater" to "Study of acting, directing, and producing plays and performances",
            "Dance" to "Study of movement and expression through dance"
        ),
        "Physical Education" to mapOf(
            "Health Education" to "Study of personal health, wellness, and physical fitness",
            "Sports Management" to "Study of the business and organization of sports events and teams",
            "Kinesiology" to "Study of human movement and physical performance",
            "Team Sports" to "Study of sports that involve teams, such as football, soccer, and basketball",
            "Individual Sports" to "Study of sports that involve individuals, such as tennis, swimming, and running"
        )
    )

    init {
        if (courses.isEmpty()) {
            departmentCourses.forEach { (department, courseAndDescription) ->
                courseAndDescription.forEach { (name, description) ->
                    courses.add(CourseDTO(
                        courseCode = (100..999).random().toString(),
                        courseName = name,
                        description = description,
                        creditHours = (70..95).random(),
                        department = department,
                        prerequisites = emptyList()
                    ))
                }
            }
        }
    }

}


class Faker(
    private val highestAge: Int = 25, private val lowestAge: Int = 18
) {
    private lateinit var student: Student
    private lateinit var course: Course

    fun student(): Student {
        student = Student(highestAge = highestAge, lowestAge = lowestAge)
        return student
    }

    fun course(): Course {
        course = Course()
        return course
    }
}

fun main() {
    repeat(10) {
        val faker = Faker()
        println(faker.student().name().firstName())
        println(faker.student().name().lastName())
        println(faker.student().name().gender())
        println()
    }
}