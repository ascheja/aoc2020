package net.sinceat.aoc2020.day8

import java.util.*

fun main() {
    println(executeBootCode(readBootCode("testinput.txt")))

    val unpatchedBootCode = readBootCode("input.txt")
    println(executeBootCode(unpatchedBootCode).accumulator)
    println(fixBootCode(unpatchedBootCode))
}

data class ExecutionResult(val path: List<Int>, val accumulator: Int, val finished: Boolean)

fun executeBootCode(bootCode: List<BootCodeOperation>): ExecutionResult {
    var accumulator = 0
    var position = 0
    val visitedPositions = mutableListOf<Int>()
    val endOfCode = bootCode.size
    while (true) {
        val operation = bootCode[position]
        val nextPosition = when (operation.instruction) {
            "nop" -> position + 1
            "acc" -> {
                accumulator += operation.operand
                position + 1
            }
            "jmp" -> {
                position + operation.operand
            }
            else -> throw IllegalArgumentException("unknown instruction '${operation.instruction}'")
        }
        if (nextPosition in visitedPositions) {
            return ExecutionResult(visitedPositions, accumulator, false)
        }
        visitedPositions.add(position)
        position = nextPosition
        if (position >= endOfCode) {
            return ExecutionResult(visitedPositions, accumulator, true)
        }
    }
}

fun fixBootCode(unpatchedBootCode: List<BootCodeOperation>): Int {
    val unpatchedExecution = executeBootCode(unpatchedBootCode)
    val possiblePatchLines = LinkedList(unpatchedExecution.path)
    while (true) {
        val patchLine = possiblePatchLines.removeFirst()
        val operationToPatch = unpatchedBootCode[patchLine]
        if (operationToPatch.instruction == "acc") {
            continue
        }
        val patchedOperation = BootCodeOperation(
            if (operationToPatch.instruction == "nop") "jmp" else "nop",
            operationToPatch.operand
        )
        val patchedBootCode = unpatchedBootCode.toMutableList().apply {
            this[patchLine] = patchedOperation
        }.toList()
        val patchedExecutionResult = executeBootCode(patchedBootCode)
        if (!patchedExecutionResult.finished) {
            continue
        }
        return patchedExecutionResult.accumulator
    }
}

data class BootCodeOperation(val instruction: String, val operand: Int)

fun readBootCode(fileName: String): List<BootCodeOperation> {
    return ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().useLines { lines ->
        lines.filter(String::isNotBlank)
            .map { line -> line.split(" ") }
            .map { (instruction, operandString) -> BootCodeOperation(instruction, operandString.toInt()) }
            .toList()
    }
}