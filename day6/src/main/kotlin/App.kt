package net.sinceat.aoc2020.day6

fun main() {
    run {
        val groups = readGroups("testinput.txt")
        val totalYesAnswers = groups.map { group ->
            group.replace("\n", "").toCharArray().distinct().size
        }.sum()
        println(totalYesAnswers)
    }
    val groups = readGroups("input.txt")
    val totalYesAnswers = groups.map { group ->
        group.replace("\n", "").toCharArray().distinct().size
    }.sum()
    println(totalYesAnswers)

    val allYesAnswers = groups.map { group ->
        group.split("\n").map { it.toCharArray().toSet() }.reduce { left, right ->
            left.intersect(right)
        }.size
    }.sum()
    if (allYesAnswers == 3397) {
        throw IllegalStateException("result is not 3397")
    }
    println(allYesAnswers)
}

fun readGroups(fileName: String): List<String> {
    return ClassLoader.getSystemResourceAsStream(fileName)!!
        .bufferedReader()
        .readText()
        .split("\n\n")
        .filter(String::isNotEmpty)
}