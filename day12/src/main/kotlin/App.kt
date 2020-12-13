package net.sinceat.aoc2020.day12

import kotlin.math.abs

fun main() {
    println(readInstructions("testinput.txt").calculateManhattanDistancePart1())
    println(readInstructions("input.txt").calculateManhattanDistancePart1())

    println(readInstructions("testinput.txt").calculateManhattanDistancePart2())
    println(readInstructions("input.txt").calculateManhattanDistancePart2())
}

fun List<Instruction>.calculateManhattanDistancePart1(): Int {
    var facing = 90
    var xDistance = 0
    var yDistance = 0
    for (instruction in this) {
        when (instruction) {
            is Forward -> when (facing) {
                0 -> yDistance += instruction.distance
                90 -> xDistance += instruction.distance
                180 -> yDistance -= instruction.distance
                270 -> xDistance -= instruction.distance
            }
            is TurnLeft -> {
                val rawFacing = facing - instruction.degrees
                facing = if (rawFacing < 0) {
                    rawFacing + 360
                } else rawFacing
            }
            is TurnRight -> {
                val rawFacing = facing + instruction.degrees
                facing = if (rawFacing >= 360) {
                    rawFacing % 360
                } else rawFacing
            }
            is North -> yDistance += instruction.distance
            is East -> xDistance += instruction.distance
            is South -> yDistance -= instruction.distance
            is West -> xDistance -= instruction.distance
        }
    }
    return abs(xDistance) + abs(yDistance)
}

data class Point(val x: Int, val y: Int)

fun Point.north(amount: Int) = Point(x, y + amount)
fun Point.east(amount: Int) = Point(x + amount, y)
fun Point.south(amount: Int) = Point(x, y - amount)
fun Point.west(amount: Int) = Point(x - amount, y)
fun Point.rotateLeft() = Point(-y, x)
fun Point.rotateRight() = Point(y, -x)

fun List<Instruction>.calculateManhattanDistancePart2(): Int {
    var waypoint = Point(10, 1)
    var xDistance = 0
    var yDistance = 0
    for (instruction in this) {
        when (instruction) {
            is Forward -> {
                xDistance += waypoint.x * instruction.distance
                yDistance += waypoint.y * instruction.distance
            }
            is TurnLeft -> repeat(instruction.degrees / 90) {
                waypoint = waypoint.rotateLeft()
            }
            is TurnRight -> repeat(instruction.degrees / 90) {
                waypoint = waypoint.rotateRight()
            }
            is North -> waypoint = waypoint.north(instruction.distance)
            is East -> waypoint = waypoint.east(instruction.distance)
            is South -> waypoint = waypoint.south(instruction.distance)
            is West -> waypoint = waypoint.west(instruction.distance)
        }
    }
    return abs(xDistance) + abs(yDistance)
}

sealed class Instruction

class Forward(val distance: Int) : Instruction()

class TurnLeft(val degrees: Int) : Instruction()

class TurnRight(val degrees: Int) : Instruction()

class North(val distance: Int) : Instruction()

class East(val distance: Int) : Instruction()

class South(val distance: Int) : Instruction()

class West(val distance: Int) : Instruction()

fun readInstructions(fileName: String): List<Instruction> {
    return ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().useLines { lines ->
        lines.filter(String::isNotBlank).map { line ->
            val instructionType = line[0]
            val instructionArgument = line.removePrefix(instructionType.toString()).toInt()
            when (instructionType) {
                'F' -> Forward(instructionArgument)
                'L' -> TurnLeft(instructionArgument)
                'R' -> TurnRight(instructionArgument)
                'N' -> North(instructionArgument)
                'E' -> East(instructionArgument)
                'S' -> South(instructionArgument)
                'W' -> West(instructionArgument)
                else -> TODO("unhandled instruction type '$instructionType'")
            }
        }.toList()
    }
}
