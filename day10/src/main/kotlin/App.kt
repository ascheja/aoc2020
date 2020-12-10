package net.sinceat.aoc2020.day10

fun main() {
    println(differences(parseFile("testinput.txt")))
    println(differences(parseFile("input.txt")))

    println(with(Cached()) { parseFile("testinput.txt").combinations() })
    println(
        with(Cached()) {
            val result = parseFile("input.txt").combinations()
            result
        }
    )
}

fun parseFile(fileName: String): List<Int> {
    return ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().useLines { lines ->
        lines.filter(String::isNotBlank).map(String::toInt).sorted().toList()
    }
}

fun differences(input: List<Int>): Int {
    var oneDiffs = 0
    var threeDiffs = 1
    var lastJoltage = 0
    for (adapterJoltage in input) {
        when (adapterJoltage - lastJoltage) {
            1 -> oneDiffs++
            3 -> threeDiffs++
        }
        lastJoltage = adapterJoltage
    }
    return oneDiffs * threeDiffs
}

class Cached {

    private val cache = mutableMapOf<Pair<List<Int>, Int>, Long>()

    fun List<Int>.combinations(last: Int = 0): Long {
        return cache.computeIfAbsent(Pair(this, last)) {
            if (isEmpty()) {
                return@computeIfAbsent 0
            }
            val head = first()
            val diff = head - last
            if (diff > 3) {
                return@computeIfAbsent 0
            }
            val tail = subList(1, size)
            if (tail.isEmpty()) {
                return@computeIfAbsent 1
            }
            return@computeIfAbsent tail.combinations(last) + tail.combinations(head)
        }
    }
}

