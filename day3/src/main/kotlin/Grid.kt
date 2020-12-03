package net.sinceat.aoc2020.day3

class Grid(private val input: List<String>) {

    private val width = input[0].length

    companion object {
        fun fromResource(fileName: String): Grid {
            return Grid(Grid::class.java.getResourceAsStream(fileName).bufferedReader().use { it.readLines() })
        }

        private const val TREE = '#'
    }

    fun slide(vector: Vector): Int {
        var currentPosition = Vector(0, 0)
        var ouch = 0
        do {
            if (input[currentPosition.x, currentPosition.y] == TREE) ouch++
            currentPosition += vector
        } while (currentPosition.y < input.size)
        return ouch
    }

    private operator fun List<String>.get(x: Int, y: Int): Char = this[y][x]

    private operator fun Vector.plus(other: Vector): Vector {
        return Vector((x + other.x) % width, y + other.y)
    }
}