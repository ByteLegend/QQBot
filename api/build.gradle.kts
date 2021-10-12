plugins {
    kotlin("jvm")
    `java-library`
}

dependencies {
    api(platform("org.jetbrains.kotlin:kotlin-bom"))
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0")
    api("org.slf4j:slf4j-api:1.7.30")
    api("com.fasterxml.jackson.core:jackson-core:2.12.3")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3") {
        exclude(group = "org.jetbrains.kotlin")
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile::class.java) {
    kotlinOptions.jvmTarget = "11"
}
