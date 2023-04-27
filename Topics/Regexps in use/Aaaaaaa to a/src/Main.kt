fun main() {
    val text = readln()
    val regex: Regex = "a+|A+".toRegex()
    var processedString = text.replace(regex, "a")
    processedString = processedString.replace(regex, "a")
    println(processedString)
}