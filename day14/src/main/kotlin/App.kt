package net.sinceat.aoc2020.day14

import kotlin.properties.Delegates

fun main() {
    println(part1InstructionProcessor(readInstructions("testinput.txt")))
    println(part1InstructionProcessor(readInstructions("input.txt")))

    println(part2InstructionProcessor(readInstructions("testinput2.txt")))
    println(part2InstructionProcessor(readInstructions("input.txt")))
}

fun part1InstructionProcessor(instructions: List<Instruction>): Long {
    var currentMask: Mask by Delegates.notNull()
    val memory = mutableMapOf<Long, Long>()
    for (instruction in instructions) {
        when (instruction) {
            is SetMask -> currentMask = instruction.mask
            is WriteMemory -> memory[instruction.address] = currentMask.apply(instruction.value)
        }
    }
    return memory.values.sum()
}

fun part2InstructionProcessor(instructions: List<Instruction>): Long {
    var currentMask: Mask by Delegates.notNull()
    val memory = mutableMapOf<Long, Long>()
    for (instruction in instructions) {
        when (instruction) {
            is SetMask -> currentMask = instruction.mask
            is WriteMemory -> {
                val addresses = currentMask.addressMasks(instruction.address)
                addresses.forEach { address ->
                    memory[address] = instruction.value
                }
            }
        }
    }
    return memory.values.sum()
}

class Mask(private val value: String) {

    private val orMask: Long = value.replace('X', '0').toLong(2)
    private val andMask: Long = value.replace('X', '1').toLong(2)

    fun apply(value: Long): Long {
        return value and andMask or orMask
    }

    fun addressMasks(address: Long): List<Long> {
        return maskAddress(address.toString(2).padStart(36, '0'), value).map { it.toLong(2) }.toList()
    }

    companion object {
        private fun maskAddress(address: String, mask: String): Sequence<String> {
            if (mask.isEmpty() || address.length != mask.length) {
                return emptySequence()
            }
            val maskHead = mask.first()
            val addressHead = address.first()
            val maskTail = mask.substring(1 until mask.length)
            val addressTail = address.substring(1 until address.length)
            if (maskTail.isEmpty()) {
                return sequence {
                    when (maskHead) {
                        '0' -> yield("$addressHead")
                        '1' -> yield("1")
                        'X' -> {
                            yield("0")
                            yield("1")
                        }
                    }
                }
            }
            return sequence {
                when (maskHead) {
                    '0' -> yieldAll(maskAddress(addressTail, maskTail).map { "$addressHead$it" })
                    '1' -> yieldAll(maskAddress(addressTail, maskTail).map { "1$it" })
                    'X' -> {
                        val tails = maskAddress(addressTail, maskTail).toList()
                        yieldAll(tails.map { "0$it" })
                        yieldAll(tails.map { "1$it" })
                    }
                }
            }
        }
    }
}

sealed class Instruction

data class SetMask(val mask: Mask) : Instruction()

data class WriteMemory(val address: Long, val value: Long) : Instruction()

fun readInstructions(fileName: String): List<Instruction> {
    return ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().useLines { lines ->
        lines.filter(String::isNotBlank).map { line -> line.split("=").map(String::trim) }.map { (left, right) ->
            if (left == "mask") {
                return@map SetMask(Mask(right))
            }
            WriteMemory(left.removePrefix("mem[").removeSuffix("]").toLong(), right.toLong())
        }.toList()
    }
}
