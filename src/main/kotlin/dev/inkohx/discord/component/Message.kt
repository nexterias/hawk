package dev.inkohx.discord.component

import kotlinx.serialization.Serializable

/**
 * https://discord.com/developers/docs/resources/webhook#execute-webhook-jsonform-params
 */
@Serializable
data class Message(
    val content: String?,
    val embeds: List<Embed>?,
) {
    class Builder {
        var content: String? = null
        var embeds: MutableList<Embed> = mutableListOf()

        fun embed(init: Embed.Builder.() -> Unit) = embeds.add(Embed.Builder().build(init))

        fun build(init: Builder.() -> Unit): Message {
            init()

            return Message(content, embeds)
        }
    }
}

fun message(init: Message.Builder.() -> Unit): Message = Message.Builder().build(init)