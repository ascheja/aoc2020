package net.sinceat.aoc2020.day13


fun main() {
    run {
        val (currentTime, busIds) = readInputPart1("testinput.txt")
        val (busId, nextDeparture) = busIds.nextDepartures(currentTime).minByOrNull { (_, nextDeparture) ->
            nextDeparture
        }!!
        println("$busId x ($nextDeparture - $currentTime) = ${busId * (nextDeparture - currentTime)}")
    }
    run {
        val (currentTime, busIds) = readInputPart1("input.txt")
        val (busId, nextDeparture) = busIds.nextDepartures(currentTime).minByOrNull { (_, nextDeparture) ->
            nextDeparture
        }!!
        println("$busId x ($nextDeparture - $currentTime) = ${busId * (nextDeparture - currentTime)}")
        println()
    }
    run {
        println(readInputPart2("testinput.txt").findFirstConsecutiveDepartures())
    }
    run {
        println(readInputPart2("input.txt").findFirstConsecutiveDepartures())
    }
}

fun List<Pair<Int, Int>>.findFirstConsecutiveDepartures(): Long {
    var time = 0L
    var stepSize = 1L
    for ((busId, offset) in this) {
        while ((time + offset) % busId != 0L) {
            time += stepSize
        }
        stepSize *= busId
    }
    return time
}

fun List<Int>.nextDepartures(currentTime: Int): Map<Int, Int> {
    return associate { busId ->
        Pair(
            busId,
            (currentTime / busId) * busId + busId
        )
    }
}

fun readInputPart1(fileName: String): Pair<Int, List<Int>> {
    val lines = ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().readLines()
    val currentTime = lines[0].toInt()
    val busIds = lines[1].split(",").mapNotNull(String::toIntOrNull)
    return Pair(currentTime, busIds)
}

fun readInputPart2(fileName: String): List<Pair<Int, Int>> {
    val lines = ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().readLines()
    return lines[1].split(",").map(String::toIntOrNull).mapIndexed { index, busId ->
        if (busId == null) {
            return@mapIndexed null
        }
        Pair(busId, index)
    }.filterNotNull()
}

