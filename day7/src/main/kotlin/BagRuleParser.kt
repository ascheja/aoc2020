package net.sinceat.aoc2020.day7

class BagRuleParser(private val rules: Map<String, List<Pair<Int, String>>>) {

    fun unpackShinyGoldBags(): Sequence<String> {
        return unpackBag("shiny gold")
    }

    private fun unpackBag(toUnpack: String): Sequence<String> = sequence {
        rules[toUnpack]?.forEach { (count, content) ->
            repeat(count) {
                yield(content)
                yieldAll(unpackBag(content))
            }
        }
    }

    fun findShinyGoldBags(): Sequence<String> {
        return findBags("shiny gold")
    }

    private fun findBags(toFind: String): Sequence<String> = sequence {
        rules.filter { (_, contents) ->
            toFind in contents.map(Pair<Int, String>::second)
        }.forEach { (container, _) ->
            yield(container)
            yieldAll(findBags(container))
        }
    }

    companion object {
        private val bagRuleRegex = Regex("(?<target>.*) bags contain (?<contents>.*)")
        private val contentRegex = Regex("(?<count>\\d+) (?<bag>\\w+ \\w+) bag[s]?[.]?")
        fun parse(file: String): BagRuleParser {
            val result = ClassLoader.getSystemResourceAsStream(file)!!.bufferedReader().use { reader ->
                reader.lineSequence().mapNotNull(bagRuleRegex::find).map { matchResult ->
                    val (target, contentsString) = matchResult.destructured
                    val contents = when (contentsString) {
                        "no other bags." -> emptyList()
                        else -> contentsString.split(", ").mapNotNull(contentRegex::find).map { contentMatch ->
                            contentMatch.groupValues[1].toInt() to contentMatch.groupValues[2]
                        }
                    }
                    target to contents
                }.toMap()
            }
            return BagRuleParser(result)
        }
    }
}