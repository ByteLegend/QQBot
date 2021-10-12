package com.bytelegend.qqbot.api

interface Message {
}

interface MessageBuilder {
    fun at(targetQQ: String): MessageBuilder
    fun plainText(content: String): MessageBuilder
    fun build(): Message
}
