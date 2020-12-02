rootProject.name = "aoc2020"

val subprojects = rootProject.projectDir
    .listFiles()!!
    .filter { it.isDirectory && it.name.startsWith("day") }
    .map { ":${it.name}" }


include(*subprojects.toTypedArray())