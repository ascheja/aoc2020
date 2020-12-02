package net.sinceat.aoc2020.day2

fun main() {
    data class PasswordPolicy(val first: Int, val second: Int, val character: Char)
    data class DatabaseEntry(val policy: PasswordPolicy, val password: String)
    val entries = ClassLoader.getSystemResource("input.txt").openStream().bufferedReader().use {
        it.lineSequence()
            .map { line ->
                val (left, password) = line.split(":").map(String::trim)
                val (rangeString, character) = left.split(" ")
                val (first, second) = rangeString.split("-").map(String::toInt)
                DatabaseEntry(PasswordPolicy(first, second, character[0]), password)
            }
            .toList()
    }
    println(
        entries.count { (policy, password) ->
            password.filter { it == policy.character }.length in policy.first .. policy.second
        }
    )

    println(
        entries.count { (policy, password) ->
            (password[policy.first - 1] == policy.character) xor (password[policy.second - 1] == policy.character)
        }
    )
}