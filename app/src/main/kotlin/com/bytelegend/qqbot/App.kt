package com.bytelegend.qqbot

import com.bytelegend.qqbot.api.Environment
import com.bytelegend.qqbot.api.QQBotService
import java.util.ServiceLoader

data class ConnectQQToPlayerRequest(
    val qq: String,
    val secret: String
)

fun main() {
    val botService = ServiceLoader.load(QQBotService::class.java).first()
    val environment = Environment.fromSystemProperty()

    botService.onJoinGroupRequest(environment.managedGroupId) { memberJoinGroupRequest ->
        JoinGroupRequestHandler(environment, botService).handle(memberJoinGroupRequest)
    }
}
