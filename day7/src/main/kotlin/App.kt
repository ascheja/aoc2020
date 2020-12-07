package net.sinceat.aoc2020.day7

fun main() {
    println(BagRuleParser.parse("testinput.txt").findShinyGoldBags().distinct().count())
    println(BagRuleParser.parse("input.txt").findShinyGoldBags().distinct().count())
    println(BagRuleParser.parse("input.txt").unpackShinyGoldBags().count())
}