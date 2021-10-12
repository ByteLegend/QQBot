package com.bytelegend.qqbot.api

data class Environment(
    val apiBaseUrl: String,
    val apiSecret: String,
    val botQQId: String,
    val botQQPassword: String,
    val managedGroupId: String
) {
    companion object {
        private fun systemProperty(name: String) = System.getProperty(name) ?: throw IllegalArgumentException("-D$name not set!")

        fun fromSystemProperty() = Environment(
            systemProperty("apiBaseUrl"),
            systemProperty("apiSecret"),
            systemProperty("botQQId"),
            systemProperty("botQQPassword"),
            systemProperty("managedGroupId"),
        )
    }
}
