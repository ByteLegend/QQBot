package com.bytelegend.qqbot.api

interface MemberJoinGroupRequest {
    val requesterQQ: String
    val requestMessage: String
}

interface QQBotService {
    fun messageBuilder(): MessageBuilder
    fun onJoinGroupRequest(groupId: String, handler: suspend (MemberJoinGroupRequest) -> Unit)
    suspend fun approveJoinGroupRequest(request: MemberJoinGroupRequest)
    suspend fun rejectJoinGroupRequest(request: MemberJoinGroupRequest, message: String)
    suspend fun sendMessage(groupId: String, message: Message)
}
