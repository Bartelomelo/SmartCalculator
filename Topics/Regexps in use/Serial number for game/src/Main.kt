fun findSerialNumberForGame(listGames: List<String>) {
    val gameName = readln()
    val regex = "${gameName}.+".toRegex()
    for (gameDetails in listGames) {
        if (gameDetails.matches(regex)) {
            val serialCode = gameDetails.split("@")
            println("Code for Xbox - ${serialCode[1]}, for PC - ${serialCode[2]}")
        }
    }
}
