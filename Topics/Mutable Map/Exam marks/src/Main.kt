fun main() {
    val studentsMarks = mutableMapOf<String, Int>()
    while (true) {
        val name = readln()
        if (name.lowercase() == "stop") {
            break
        } else if (studentsMarks.keys.contains(name)){
            val age = readln().toInt()
            continue
        } else {
            val age = readln().toInt()
            studentsMarks += name to age
        }

    }
    println(studentsMarks)
}