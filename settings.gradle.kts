/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/7.2/userguide/multi_project_builds.html
 */
pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.5.31"
    }
}
rootProject.name = "QQBot"
include("app")
include("api")
include("mirai-impl")
