package net.sinceat.aoc2020.day5

fun main() {
    val testCodes = listOf(
        "FBFBBFFRLR",
        "BFFFBBFRRR",
        "FFFBBBFRRR",
        "BBFFBBFRLL"
    )
    testCodes.forEach { testCode ->
        println(SeatCoordinates.ofSeatCode(testCode))
    }

    ClassLoader.getSystemResourceAsStream("input.txt")!!.bufferedReader().useLines { lines ->
        val maxSeatId = lines.filterNot(String::isEmpty)
            .map(SeatCoordinates.Companion::ofSeatCode)
            .map(SeatCoordinates::seatId)
            .maxOrNull()
        println("max seat id: $maxSeatId")
    }

    ClassLoader.getSystemResourceAsStream("input.txt")!!.bufferedReader().useLines { lines ->
        val emptySeatId = lines.filterNot(String::isEmpty)
            .map(SeatCoordinates.Companion::ofSeatCode)
            .map(SeatCoordinates::seatId)
            .sorted()
            .windowed(2, 1)
            .first { (a, b) -> b - a == 2 }[0] + 1
        println("empty seat id: $emptySeatId")
    }
}

data class SeatCoordinates(val row: Int, val column: Int, val seatId: Int = row * 8 + column) {

    companion object {
        private fun codeToNumber(code: String, highChar: Char): Int {
            return code.map { c -> if (c == highChar) 1 else 0 }.reduce { a, b -> a * 2 + b }
        }

        fun ofSeatCode(code: String): SeatCoordinates {
            val (rowCode, columnCode) = code.chunked(7)
            return SeatCoordinates(codeToNumber(rowCode, 'B'), codeToNumber(columnCode, 'R'))
        }
    }
}

