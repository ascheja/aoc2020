package net.sinceat.aoc2020.day16

import kotlin.properties.Delegates

fun main() {
    run {
        val parseResult = parseInput("testinput.txt")
        println(parseResult.calculateScanningErrorRate())
    }
    run {
        val parseResult = parseInput("input.txt")
        println(parseResult.calculateScanningErrorRate())
    }
    run {
        val parseResult = parseInput("testinput2.txt").filterCompletelyInvalid()
        val assignedFields = parseResult.assignFields()
        println(assignedFields)
    }
    run {
        val parseResult = parseInput("input.txt").filterCompletelyInvalid()
        val assignedFields = parseResult.assignFields()
        println(assignedFields)
        val departureFields = assignedFields.filterKeys { it.startsWith("departure") }
        println(departureFields.values.map { index -> parseResult.myTicket[index].toLong() }.reduce { acc, i -> acc * i })
    }
}

fun ParseResult.filterCompletelyInvalid(): ParseResult {
    return ParseResult(
        rules,
        myTicket,
        nearbyTickets.filter { ticket ->
            ticket.all { value -> rules.values.any { rule -> value in rule } }
        }
    )
}

fun ParseResult.assignFields(): Map<String, Int> {
    val ticketColumns = myTicket.indices.map { index ->
        nearbyTickets.map { ticket -> ticket[index] }
    }
    val candidates = ticketColumns.map { columnValues ->
        rules.filter { (_, rule) ->
            columnValues.all { it in rule }
        }.map { it.key }
    }
    val alreadyUsed = mutableSetOf<String>()
    return candidates.withIndex().sortedBy { it.value.size }.associate { (index, values) ->
        val remaining = values.filterNot { it in alreadyUsed }.single()
        alreadyUsed.add(remaining)
        Pair(remaining, index)
    }
}

fun ParseResult.calculateScanningErrorRate(): Int {
    return nearbyTickets.map { ticket ->
        ticket.filterNot { value -> rules.values.any { rule -> value in rule } }.sum()
    }.sum()
}

data class Rule(val range1: IntRange, val range2: IntRange) {
    operator fun contains(value: Int) = value in range1 || value in range2
}

data class ParseResult(val rules: Map<String, Rule>, val myTicket: List<Int>, val nearbyTickets: List<List<Int>>)


private enum class FileSection {
    RULES,
    MY_TICKET,
    OTHER_TICKETS
}

fun parseInput(fileName: String): ParseResult {
    return ClassLoader.getSystemResourceAsStream(fileName)!!.bufferedReader().useLines { lines ->
        var section = FileSection.RULES
        val rules = mutableMapOf<String, Rule>()
        var myTicket by Delegates.notNull<List<Int>>()
        val nearbyTickets = mutableListOf<List<Int>>()
        lines.filter(String::isNotBlank).forEach { line ->
            when (line) {
                "your ticket:" -> section = FileSection.MY_TICKET
                "nearby tickets:" -> section = FileSection.OTHER_TICKETS
                else -> if (section == FileSection.RULES) {
                    val (fieldName, ruleRangesString) = line.split(":").map(String::trim)
                    val rule = ruleRangesString.split(" or ")
                        .map { ruleRangeString ->
                            ruleRangeString.split("-")
                                .map(String::toInt)
                                .let { (start, endInclusive) -> start..endInclusive }
                        }.let { (range1, range2) -> Rule(range1, range2) }
                    rules[fieldName] = rule
                } else {
                    val ticket = line.split(",").map(String::toInt)
                    if (section == FileSection.MY_TICKET) {
                        myTicket = ticket
                    } else {
                        nearbyTickets.add(ticket)
                    }
                }
            }
        }
        ParseResult(rules, myTicket, nearbyTickets)
    }
}