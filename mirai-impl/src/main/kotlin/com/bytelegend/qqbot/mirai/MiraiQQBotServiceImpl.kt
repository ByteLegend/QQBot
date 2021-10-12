package com.bytelegend.qqbot.mirai

import com.bytelegend.qqbot.api.Environment
import com.bytelegend.qqbot.api.HttpApiClient
import com.bytelegend.qqbot.api.MemberJoinGroupRequest
import com.bytelegend.qqbot.api.Message
import com.bytelegend.qqbot.api.MessageBuilder
import com.bytelegend.qqbot.api.QQBotService
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.events.MemberJoinRequestEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.BotConfiguration

class MiraiMemberJoinRequestAdapter(val delegate: MemberJoinRequestEvent) : MemberJoinGroupRequest {
    override val requesterQQ: String = delegate.fromId.toString()
    override val requestMessage: String = delegate.message
}

class MiraiMessageAdapter(val delegate: net.mamoe.mirai.message.data.Message) : Message {
}

class MiraiMessageBuilder : MessageBuilder {
    private val messages = mutableListOf<net.mamoe.mirai.message.data.Message>()
    override fun at(targetQQ: String): MessageBuilder {
        messages.add(At(targetQQ.toLong()))
        return this
    }

    override fun plainText(content: String): MessageBuilder {
        messages.add(PlainText(content))
        return this
    }

    override fun build(): Message {
        var messageChain: net.mamoe.mirai.message.data.Message = EmptyMessageChain
        messages.forEach {
            messageChain = messageChain.plus(it)
        }
        return MiraiMessageAdapter(messageChain)
    }
}

class MiraiQQBotServiceImpl : QQBotService {
    private val environment = Environment.fromSystemProperty()
    private val httpApiClent = HttpApiClient(environment)
    private val bot: Bot = runBlocking {
        BotFactory.newBot(environment.botQQId.toLong(), environment.botQQPassword) {
            val deviceJson = httpApiClent.GET("${environment.apiBaseUrl}/deviceJson").apply {
                require(statusCode() in 200..300) { "Error: $this ${body()}" }
            }.body()
            loadDeviceInfoJson(deviceJson)
            protocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE // 切换协议
        }.alsoLogin()
    }

    override fun messageBuilder(): MessageBuilder = MiraiMessageBuilder()

    override fun onJoinGroupRequest(groupId: String, handler: suspend (MemberJoinGroupRequest) -> Unit) {
        bot.eventChannel.subscribeAlways<MemberJoinRequestEvent> {
            handler(MiraiMemberJoinRequestAdapter(this))
        }
    }

    override suspend fun approveJoinGroupRequest(request: MemberJoinGroupRequest) {
        request as MiraiMemberJoinRequestAdapter
        request.delegate.accept()
    }

    override suspend fun rejectJoinGroupRequest(request: MemberJoinGroupRequest, message: String) {
        request as MiraiMemberJoinRequestAdapter
        request.delegate.reject(false, message)
    }

    override suspend fun sendMessage(groupId: String, message: Message) {
        bot.getGroup(groupId.toLong())!!.sendMessage((message as MiraiMessageAdapter).delegate)
    }
}
