package net.sinceat.aoc2020.day4

class Passport(map: Map<String, String>) {
    val byr: Int? = map["byr"]?.toInt()
    val iyr: Int? = map["iyr"]?.toInt()
    val eyr: Int? = map["eyr"]?.toInt()
    val hgt: String? = map["hgt"]
    val hcl: String? = map["hcl"]
    val ecl: String? = map["ecl"]
    val pid: String? = map["pid"]
    val cid: String? = map["cid"]

    override fun toString(): String {
        return "Passport(byr=$byr,iyr=$iyr,eyr=$eyr,hgt=$hgt,hcl=$hcl,ecl=$ecl,pid=$pid,cid=$cid)"
    }
}

fun main() {
    val passports = ClassLoader.getSystemResourceAsStream("input.txt")!!.bufferedReader().use { reader ->
        reader.readText().split("\n\n").filter(String::isNotBlank).map { passportString ->
            Passport(
                passportString
                    .replace('\n', ' ')
                    .split(" ")
                    .filter { it.isNotEmpty() }
                    .associate { pair ->
                        pair.split(":").let { (a, b) -> Pair(a, b) }
                    }
            )
        }
    }
    run {
        fun Passport.isValid(): Boolean {
            return listOf(byr, iyr, eyr, hgt, hcl, ecl, pid).none { it == null }
        }
        println(passports.count { it.isValid() })
    }
    run {
        val hairColorRegex = Regex("#[0-9a-fA-F]{6}")
        val validEyeColors = "amb blu brn gry grn hzl oth".split(" ").toSet()
        val passIdRegex = Regex("[0-9]{9}")
        fun Passport.isValid(): Boolean {
            if (listOf(byr, iyr, eyr, hgt, hcl, ecl, pid).any { it == null }) return false
            if (byr !in 1920 .. 2002) return false
            if (iyr !in 2010 .. 2020) return false
            if (eyr !in 2020 .. 2030) return false
            if ("cm" !in hgt!! && "in" !in hgt) return false
            if ("cm" in hgt && hgt.removeSuffix("cm").toIntOrNull() !in 150 .. 193) return false
            if ("in" in hgt && hgt.removeSuffix("in").toIntOrNull() !in 59..76) return false
            if (!hcl!!.matches(hairColorRegex)) return false
            if (ecl!! !in validEyeColors) return false
            return pid!!.matches(passIdRegex)
        }
        println(passports.count { it.isValid() })
    }
}