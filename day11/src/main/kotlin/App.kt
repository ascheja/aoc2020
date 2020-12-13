package net.sinceat.aoc2020.day11

import kotlin.math.abs
import kotlin.math.sign
import net.sinceat.aoc2020.day11.Part1EvolutionRules.evolveUntilStable

fun main() {
    with (Part1EvolutionRules) {
        run {
            val fullyEvolvedGrid = readGrid("testinput.txt").evolveUntilStable().last()
            Grid.compare(
                readGrid("testoutput.txt"),
                fullyEvolvedGrid
            )
            println(fullyEvolvedGrid.occupiedSeats)
        }
        run {
            val fullyEvolvedGrid = readGrid("input.txt").evolveUntilStable().last()
            println(fullyEvolvedGrid.occupiedSeats)
        }
    }
    with (Part2EvolutionRules) {
        run {
            val fullyEvolvedGrid = readGrid("testinput.txt").evolveUntilStable().last()
            Grid.compare(
                readGrid("testoutputp2.txt"),
                fullyEvolvedGrid
            )
            println(fullyEvolvedGrid.occupiedSeats)
        }
        run {
            val fullyEvolvedGrid = readGrid("input.txt").evolveUntilStable().last()
            println(fullyEvolvedGrid.occupiedSeats)
        }
    }
}

private fun readGrid(fileName: String): Grid {
    return ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().useLines { lines ->
        Grid(lines.filter(String::isNotBlank).map(String::toList).toList())
    }
}

private class Grid(val data: List<List<Char>>) {

    companion object {
        fun compare(grid1: Grid, grid2: Grid) {
            if (grid1.width != grid2.width || grid1.data.size != grid2.data.size) {
                println("grid dimensions not equal")
                return
            }
            for ((a, b) in grid1.data.zip(grid2.data)) {
                val aString = a.joinToString("")
                val bString = b.joinToString("")
                println("$aString  $bString  ${if (aString == bString) "OK" else "ERROR"}")
            }
        }
    }

    val width = data[0].size

    val occupiedSeats by lazy {
        data.sumBy { row -> row.count { it == '#' } }
    }

    override fun toString(): String {
        return data.joinToString("\n") { it.joinToString("") }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Grid) return false

        if (width != other.width) return false
        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + width
        return result
    }
}

private data class Point(val row: Int, val column: Int) {
    operator fun plus(v: Vector) = Point(row + v.rowDir, column + v.colDir)
}

private data class Vector(val rowDir: Int, val colDir: Int) {
    fun extend(): Vector {
        return Vector(
            rowDir.sign * (abs(rowDir) + 1),
            colDir.sign * (abs(colDir) + 1)
        )
    }
}

private abstract class GridEvolutionRules {

    protected abstract val acceptableNeighbors: Int

    protected abstract fun Grid.hasVisibleNeighbor(start: Point, vector: Vector): Boolean

    private fun Grid.countNeighbors(rowNr: Int, columnNr: Int): Int {
        val vectors = listOf(
            Vector(-1, -1),
            Vector(-1, 0),
            Vector(-1, 1),
            Vector(0, -1),
            Vector(0, 1),
            Vector(1, -1),
            Vector(1, 0),
            Vector(1, 1),
        )
        val p = Point(rowNr, columnNr)
        return vectors.count { vector -> hasVisibleNeighbor(p, vector) }
    }

    fun Grid.evolve(): Grid {
        return Grid(
            List(data.size) { rowNr ->
                List(width) { columnNr ->
                    val status = data[rowNr][columnNr]
                    if (status == '.') {
                        '.'
                    } else {
                        val neighbors = countNeighbors(rowNr, columnNr)
                        when {
                            neighbors == 0 -> '#'
                            neighbors > acceptableNeighbors -> 'L'
                            else -> status
                        }
                    }
                }
            }
        )
    }

    fun Grid.evolveUntilStable(): Sequence<Grid> = sequence {
        var current = this@evolveUntilStable
        while (true) {
            val next = current.evolve()
            if (current == next) {
                break
            }
            current = next
            yield(current)
        }
    }
}

private object Part1EvolutionRules : GridEvolutionRules() {

    override val acceptableNeighbors: Int = 3

    override fun Grid.hasVisibleNeighbor(start: Point, vector: Vector): Boolean {
        val p = start + vector
        if (p.row !in data.indices || p.column !in 0 until width) {
            return false
        }
        return when (data[p.row][p.column]) {
            '#' -> true
            else -> false
        }
    }
}

private object Part2EvolutionRules : GridEvolutionRules() {

    override val acceptableNeighbors: Int = 4

    override tailrec fun Grid.hasVisibleNeighbor(start: Point, vector: Vector): Boolean {
        val p = start + vector
        if (p.row !in data.indices || p.column !in 0 until width) {
            return false
        }
        return when (data[p.row][p.column]) {
            '#' -> true
            'L' -> false
            else -> hasVisibleNeighbor(start, vector.extend())
        }
    }
}