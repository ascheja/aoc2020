package net.sinceat.aoc2020.day17

fun main() {
    run {
        var grid = Grid(readInput("testinput.txt") { x, y -> GridPoint3d(x, y, 0) }, ::drawGrid3d)
        (1 .. 6).forEach {
            grid = grid.evolve()
        }
        println(grid.countActive())
        println()
    }
    run {
        var grid = Grid(readInput("input.txt") { x, y -> GridPoint3d(x, y, 0) }, ::drawGrid3d)
        (1 .. 6).forEach {
            grid = grid.evolve()
        }
        println(grid.countActive())
        println()
    }
    run {
        var grid = Grid(readInput("testinput.txt") { x, y -> GridPoint4d(x, y, 0, 0) }, ::drawGrid4d)
        (1 .. 6).forEach {
            grid = grid.evolve()
        }
        println(grid.countActive())
        println()
    }
    run {
        var grid = Grid(readInput("input.txt") { x, y -> GridPoint4d(x, y, 0, 0) }, ::drawGrid4d)
        (1 .. 6).forEach {
            grid = grid.evolve()
        }
        println(grid.countActive())
        println()
    }
}

fun <T> readInput(fileName: String, mapper: (Int, Int) -> T): Map<T, Char> {
    return ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().useLines { lines ->
        lines.filter(String::isNotEmpty).flatMapIndexed { yIndex, line ->
            line.toCharArray().mapIndexed { xIndex, char -> if (char == '#') mapper(xIndex, yIndex) else null }.filterNotNull()
        }.associateWith { '#' }
    }
}

fun <T : GridPoint<T>> Grid<T>.evolve(): Grid<T> {
    val allActive = data.filterValues { it == '#' }.keys
    val remainingActive = allActive.filter { gridPoint ->
        gridPoint.neighbors().filter { it in allActive }.count() in 2 .. 3
    }
    val goingActive = allActive.flatMap { activeNeighbor ->
        val inactiveNeighbors = activeNeighbor.neighbors().filter { it !in allActive }
        inactiveNeighbors.map { inactiveNeighbor -> Pair(inactiveNeighbor, activeNeighbor) }
    }.groupBy { it.first }
        .mapValues { (_, pairs) -> pairs.map { it.second }.distinct().count() }
        .filterValues { it == 3 }
        .keys
    return Grid((remainingActive + goingActive).associateWith { '#' }, formatter)
}

fun <T : GridPoint<T>> Grid<T>.countActive(): Int {
    return data.size
}

fun drawGrid3d(data: Map<GridPoint3d, Char>) = buildString {
    if (data.isEmpty()) {
        return ""
    }
    val xs = data.keys.map(GridPoint3d::x)
    val ys = data.keys.map(GridPoint3d::y)
    val zs = data.keys.map(GridPoint3d::z)
    val xRange = xs.minOrNull()!! .. xs.maxOrNull()!!
    val yRange = ys.minOrNull()!! .. ys.maxOrNull()!!
    val zRange = zs.minOrNull()!! .. zs.maxOrNull()!!
    for (z in zRange) {
        appendLine("z=$z")
        for (y in yRange) {
            for (x in xRange) {
                append(data[GridPoint3d(x, y, z)] ?: '.')
            }
            appendLine()
        }
        appendLine()
    }
}

fun drawGrid4d(data: Map<GridPoint4d, Char>) = buildString {
    if (data.isEmpty()) {
        return ""
    }
    val xs = data.keys.map(GridPoint4d::x)
    val ys = data.keys.map(GridPoint4d::y)
    val zs = data.keys.map(GridPoint4d::z)
    val ws = data.keys.map(GridPoint4d::w)
    val xRange = xs.minOrNull()!! .. xs.maxOrNull()!!
    val yRange = ys.minOrNull()!! .. ys.maxOrNull()!!
    val zRange = zs.minOrNull()!! .. zs.maxOrNull()!!
    val wRange = ws.minOrNull()!! .. ws.maxOrNull()!!
    for (w in wRange) {
        for (z in zRange) {
            appendLine("z=$z,w=$w")
            for (y in yRange) {
                for (x in xRange) {
                    append(data[GridPoint4d(x, y, z, w)] ?: '.')
                }
                appendLine()
            }
            appendLine()
        }
    }
}

interface GridPoint<T : GridPoint<T>> {

    fun neighbors(): List<T>

    operator fun compareTo(other: T): Int
}

data class GridPoint3d(val x: Int, val y: Int, val z: Int) : GridPoint<GridPoint3d> {

    override fun neighbors(): List<GridPoint3d> {
        return sequence {
            for (nx in (x - 1) .. (x + 1)) {
                for (ny in (y - 1) .. (y + 1)) {
                    for (nz in (z - 1) .. (z + 1)) {
                        if (nx == x && ny == y && nz == z) {
                            continue
                        }
                        yield(GridPoint3d(nx, ny, nz))
                    }
                }
            }
        }.toList()
    }

    override fun compareTo(other: GridPoint3d): Int {
        val xCmp = x.compareTo(other.x)
        if (xCmp != 0) {
            return xCmp
        }
        val yCmp = y.compareTo(other.y)
        if (yCmp != 0) {
            return yCmp
        }
        return z.compareTo(other.z)
    }
}

data class GridPoint4d(val x: Int, val y: Int, val z: Int, val w: Int) : GridPoint<GridPoint4d> {

    override fun neighbors(): List<GridPoint4d> {
        return sequence {
            for (nx in (x - 1) .. (x + 1)) {
                for (ny in (y - 1) .. (y + 1)) {
                    for (nz in (z - 1) .. (z + 1)) {
                        for (nw in (w - 1) .. (w + 1)) {
                            if (nx == x && ny == y && nz == z && nw == w) {
                                continue
                            }
                            yield(GridPoint4d(nx, ny, nz, nw))
                        }
                    }
                }
            }
        }.toList()
    }

    override fun compareTo(other: GridPoint4d): Int {
        val cmp3d = reduce().compareTo(other.reduce())
        if (cmp3d != 0) return cmp3d
        return w.compareTo(other.w)
    }

    private fun reduce(): GridPoint3d = GridPoint3d(x, y, z)
}

class Grid<T : GridPoint<T>>(val data: Map<T, Char>, val formatter: (Map<T, Char>) -> String) {

    override fun toString(): String = formatter(data)
}
