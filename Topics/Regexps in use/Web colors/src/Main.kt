fun main() {
    val text = readLine()!!
    val regexColors = "#[0-9a-fA-F]{6}\\b".toRegex()
    val colorsFound = regexColors.findAll(text)
    for (matches in colorsFound) {
        println(matches.value)
    }
}