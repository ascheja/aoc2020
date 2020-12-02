package net.sinceat.aoc2020.day1

fun main() {
    val ints = ClassLoader.getSystemResource("input.txt").openStream().use {
        it.bufferedReader().lineSequence().map(String::toInt).toList()
    }
    println(ints.combinations(2).first { it.sum() == 2020 }.product())
    println(ints.combinations(3).first { it.sum() == 2020 }.product())
}

private fun List<Int>.product(): Int {
    if (isEmpty()) {
        return 1
    }
    return first() * subList(1, size).product()
}

private fun List<Int>.combinations(n: Int): Sequence<List<Int>> {
    if (isEmpty()) return emptySequence()
    return sequence {
        when (n) {
            1 -> yieldAll(map { listOf(it) })
            else -> {
                val head = first()
                val tail = subList(1, size)
                yieldAll(tail.combinations(n - 1).map { it + head })
                yieldAll(tail.combinations(n))
            }
        }
    }
}