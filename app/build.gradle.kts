plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":api"))
    runtimeOnly(project(":mirai-impl"))
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("ch.qos.logback:logback-core:1.2.3")
}

tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile::class.java) {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("com.bytelegend.qqbot.AppKt")
}
