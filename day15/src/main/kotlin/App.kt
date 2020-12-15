package net.sinceat.aoc2020.day15

fun main() {
    println("1, 3, 2, ... = ${theElvesGamePart1(listOf(1, 3, 2)).nth(2020)}")
    println("2, 1, 3, ... = ${theElvesGamePart1(listOf(2, 1, 3)).nth(2020)}")
    println("1, 2, 3, ... = ${theElvesGamePart1(listOf(1, 2, 3)).nth(2020)}")
    println("2, 3, 1, ... = ${theElvesGamePart1(listOf(2, 3, 1)).nth(2020)}")
    println("3, 2, 1, ... = ${theElvesGamePart1(listOf(3, 2, 1)).nth(2020)}")
    println("3, 1, 2, ... = ${theElvesGamePart1(listOf(3, 1, 2)).nth(2020)}")

    println("5,2,8,16,18,0,1, ... = ${theElvesGamePart1(listOf(5, 2, 8, 16, 18, 0, 1)).nth(2020)}")
    println("5,2,8,16,18,0,1, ... = ${theElvesGamePart1(listOf(5, 2, 8, 16, 18, 0, 1)).nth(30000000)}")
}

fun theElvesGamePart1(startingNumbers: List<Int>) = sequence {
    val memory = mutableMapOf<Int, Int>()
    startingNumbers.subList(0, startingNumbers.size - 1).forEachIndexed { index, number ->
        yield(number)
        memory[number] = index + 1
    }
    var lastSpokenNumber = startingNumbers.last()
    var turn = startingNumbers.size
    while (true) {
        yield(lastSpokenNumber)
        val nextSpokenNumber = memory[lastSpokenNumber]?.let { turn - it } ?: 0
        memory[lastSpokenNumber] = turn
        turn++
        lastSpokenNumber = nextSpokenNumber
    }
}

fun Sequence<Int>.nth(n: Int): Int {
    return drop(n - 1).first()
}

