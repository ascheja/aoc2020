package net.sinceat.aoc2020.day9

import java.util.LinkedList

fun main() {
    val testInput = parseInput("testinput.txt")
    val testBuffer = RingBuffer.fromSequence(testInput, 5)
    println(lookForFirstInvalidNumber(testBuffer, testInput))

    val input = parseInput("input.txt")
    val invalidNumber = run {
        lookForFirstInvalidNumber(RingBuffer.fromSequence(input, 25), input)
    }
    println(invalidNumber!!)

    for (windowSize in 2 .. Integer.MAX_VALUE) {
        println("sequence length: $windowSize")
        for (window in input.windowed(windowSize, 1)) {
            val sum = window.sum()
            when {
                sum == invalidNumber -> {
                    println("found it: ${window.minOrNull()!! + window.maxOrNull()!!}")
                    return
                }
                sum > invalidNumber -> {
                    break
                }
            }
        }
    }
}

fun lookForFirstInvalidNumber(buffer: RingBuffer, input: Sequence<Long>): Long? {
    for (nr in input.drop(buffer.size)) {
        if (nr !in buffer.allSums()) {
            return nr
        }
        buffer.add(nr)
    }
    return null
}

fun RingBuffer.allSums() = sequence {
    for (i in 0 until size) {
        val first = get(i)
        for (k in i until size) {
            val second = get(k)
            yield(first + second)
        }
    }
}

fun parseInput(fileName: String) = sequence {
    ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().useLines { lines ->
        yieldAll(lines.filter(String::isNotBlank).map(String::toLong))
    }
}

class RingBuffer(private val inner: LinkedList<Long>) : List<Long> by inner {
    fun add(element: Long) {
        inner.removeFirst()
        inner.add(element)
    }

    companion object {
        fun fromSequence(sequence: Sequence<Long>, preambleLength: Int): RingBuffer {
            val preamble = LinkedList(sequence.take(preambleLength).toList())
            return RingBuffer(preamble)
        }
    }
}