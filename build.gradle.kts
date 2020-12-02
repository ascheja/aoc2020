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