package net.sinceat.aoc2020.day3

fun main() {
    val grid = Grid.fromResource("/input.txt")
    val slope = Vector(3, 1)
    println(grid.slide(slope))

    val slopes = listOf(
        Vector(1, 1),
        Vector(3, 1),
        Vector(5, 1),
        Vector(7, 1),
        Vector(1, 2)
    )
    println(slopes.map { grid.slide(it).toLong() }.reduce { a, b -> a * b })
}