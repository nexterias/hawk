package dev.inkohx.discord.component

import dev.inkohx.discord.serializer.ColorSerializer
import dev.inkohx.discord.serializer.ISODateSerializer
import kotlinx.serialization.*
import java.awt.Color
import java.time.Instant

/**
 * https://discord.com/developers/docs/resources/channel#embed-object-embed-structure
 */
@Serializable
data class Embed(
    val title: String? = null,
    @Serializable(with = ISODateSerializer::class) val timestamp: Instant? = null,
    @Serializable(with = ColorSerializer::class) val color: Color? = null,
    val fields: List<Field>? = null,
    val author: Author? = null,
    val footer: Footer? = null
) {
    /**
     * https://discord.com/developers/docs/resources/channel#embed-object-embed-author-structure
     */
    @Serializable
    data class Author(
        val name: String,
        val url: String?,
        @SerialName("icon_url") val iconUrl: String? = null,
        @SerialName("proxy_icon_url") val proxyIconUrl: String? = null
    ) {
        class Builder {
            lateinit var name: String
            var url: String? = null
            var iconUrl: String? = null
            var proxyIconUrl: String? = null

            fun build(init: Builder.() -> Unit): Author {
                init()

                return Author(name, url, iconUrl, proxyIconUrl)
            }
        }
    }

    /**
     * https://discord.com/developers/docs/resources/channel#embed-object-embed-footer-structure
     */
    @Serializable
    data class Footer(
        val text: String,
        @SerialName("icon_url") val iconUrl: String? = null,
        @SerialName("proxy_icon_url") val proxyIconUrl: String? = null
    ) {
        class Builder {
            lateinit var text: String
            var iconUrl: String? = null
            var proxyIconUrl: String? = null

            fun build(init: Builder.() -> Unit): Footer {
                init()

                return Footer(text, iconUrl, proxyIconUrl)
            }
        }
    }

    /**
     * https://discord.com/developers/docs/resources/channel#embed-object-embed-field-structure
     */
    @Serializable
    data class Field(
        val name: String,
        val value: String,
        val inline: Boolean? = null
    ) {
        class Builder {
            lateinit var name: String
            lateinit var value: String
            var inline: Boolean? = null

            fun build(init: Builder.() -> Unit): Field {
                init()

                return Field(name, value, inline)
            }
        }
    }

    class Builder {
        var title: String? = null
        var timestamp: Instant? = null
        var fields: MutableList<Field> = mutableListOf()
        var author: Author? = null
        var color: Color? = null
        var footer: Footer? = null

        fun field(init: Field.Builder.() -> Unit) = fields.add(Field.Builder().build(init))

        fun footer(init: Footer.Builder.() -> Unit) {
            footer = Footer.Builder().build(init)
        }

        fun author(init: Author.Builder.() -> Unit) {
            author = Author.Builder().build(init)
        }

        fun build(init: Builder.() -> Unit): Embed {
            init()

            return Embed(title, timestamp, color, fields, author, footer)
        }
    }
}

fun embed(init: Embed.Builder.() -> Unit) = Embed.Builder().build(init)
