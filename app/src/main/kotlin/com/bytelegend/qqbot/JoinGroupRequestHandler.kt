package com.bytelegend.qqbot

import com.bytelegend.qqbot.api.Environment
import com.bytelegend.qqbot.api.HttpApiClient
import com.bytelegend.qqbot.api.MemberJoinGroupRequest
import com.bytelegend.qqbot.api.OBJECT_MAPPER
import com.bytelegend.qqbot.api.QQBotService
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import java.util.UUID

private val SECRET_REGEX = "[a-zA-Z0-9]+".toRegex()

class JoinGroupRequestHandler(
    private val environment: Environment,
    private val botService: QQBotService
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val httpApiClient = HttpApiClient(environment)
    suspend fun handle(memberJoinGroupRequest: MemberJoinGroupRequest) {
        val matchResult = SECRET_REGEX.find(memberJoinGroupRequest.requestMessage)
        if (matchResult == null) {
            logger.info("Rejected join request $memberJoinGroupRequest because no secret found.")
            botService.rejectJoinGroupRequest(memberJoinGroupRequest, "回答不正确：未找到[a-zA-Z0-9]+")
        } else {
            val secret = matchResult.value
            val response = httpApiClient.POST(
                "${environment.apiBaseUrl}/qq/binding",
                ConnectQQToPlayerRequest(
                    memberJoinGroupRequest.requesterQQ,
                    secret
                )
            )
            if (response.statusCode() in listOf(400, 404, 409)) {
                val responseBody = OBJECT_MAPPER.readValue(response.body(), Map::class.java)
                logger.info("Rejected join request $memberJoinGroupRequest because server returns 404 on secret $secret.")
                botService.rejectJoinGroupRequest(memberJoinGroupRequest, responseBody["message"] as String)
            } else if (response.statusCode() > 300) {
                val uuid = UUID.randomUUID().toString()
                logger.error("Rejected join request $memberJoinGroupRequest because server returns unknown response:$uuid $response ${response.body()}.")
                botService.rejectJoinGroupRequest(memberJoinGroupRequest, "内部错误，请联系管理员QQ376488967并提供事件ID $uuid")
            } else {
                botService.approveJoinGroupRequest(memberJoinGroupRequest)

                delay(5000)
                val message = botService.messageBuilder().at(memberJoinGroupRequest.requesterQQ)
                    .plainText(" 欢迎加入！")
                    .build()
                botService.sendMessage("788942934", message)
            }
        }
    }
}
