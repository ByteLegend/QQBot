plugins {
    kotlin("jvm")
    `java-library`
}

dependencies {
    val miraiVersion = "2.7.1"
    implementation("net.mamoe:mirai-core:$miraiVersion")
    implementation(project(":api"))
}
