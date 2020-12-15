import java.nio.file.Files
import java.nio.file.StandardOpenOption

plugins {
    kotlin("jvm") version "1.4.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        jcenter()
    }
    apply(plugin = "kotlin")
}

dependencies {
    implementation(kotlin("stdlib"))
}

task("newDay") {
    doLast {
        val newDay = "day" + (subprojects.map { it.name.removePrefix("day").toInt() }.max()!! + 1)
        val rootPath = rootDir.toPath()
        val kotlinPath = rootPath.resolve("$newDay/src/main/kotlin")
        Files.createDirectories(kotlinPath)
        Files.write(
            kotlinPath.resolve("App.kt"),
            listOf(
                "package net.sinceat.aoc2020.$newDay",
                "",
                "fun main() {",
                """    println("Hello World!")""",
                "}",
                ""
            ).joinToString("\n").toByteArray(),
            StandardOpenOption.CREATE
        )
        Files.createDirectories(rootPath.resolve("$newDay/src/main/resources"))
    }
}
