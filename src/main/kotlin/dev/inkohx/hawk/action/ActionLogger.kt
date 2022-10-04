package dev.inkohx.hawk.action

import dev.inkohx.discord.component.message
import dev.inkohx.hawk.Hawk
import dev.inkohx.hawk.action.LogLevel.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.entity.Player
import java.awt.Color
import java.time.Instant

enum class LogLevel {
    OFF,
    INFO,
    WARN
}

class ActionLogger {
    fun log(type: ActionType, player: Player, reason: String) {
        val level = getLevel(type)

        if (level == OFF) return

        sendConsole(level, type, player, reason)
        sendOnlinePlayers(type, player, reason)
        sendDiscord(level, type, player, reason)
    }

    private fun getLevel(type: ActionType): LogLevel {
        val config = Hawk.instance.config

        if (config.getStringList("logging.level.off").contains(type.name)) return OFF
        if (config.getStringList("logging.level.info").contains(type.name)) return INFO
        if (config.getStringList("logging.level.warn").contains(type.name)) return WARN

        throw IllegalArgumentException()
    }

    private fun sendConsole(level: LogLevel, type: ActionType, player: Player, reason: String) {
        if (!Hawk.instance.config.getBoolean("logging.console")) return

        val message = "${player.name}が${type.name}で検知されました。理由: $reason"

        when (level) {
            WARN -> Hawk.instance.logger.warning(message)
            INFO -> Hawk.instance.logger.info(message)
            else -> return
        }
    }

    private fun sendOnlinePlayers(type: ActionType, player: Player, reason: String) {
        if (!Hawk.instance.config.getBoolean("logging.onlinePlayers")) return

        val component = TextComponent("[HAWK] ").apply {
            color = ChatColor.DARK_RED
            isBold = true
            addExtra(
                TextComponent(player.name).apply {
                    color = ChatColor.AQUA
                    isBold = true
                }
            )
            addExtra(
                TextComponent("が").apply {
                    color = ChatColor.RED
                }
            )
            addExtra(
                TextComponent(type.name).apply {
                    hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(reason))
                    color = ChatColor.GOLD
                    isBold = true
                }
            )
            addExtra(
                TextComponent("で検知されました。").apply {
                    color = ChatColor.RED
                }
            )
        }

        Hawk.instance.server.spigot().broadcast(component)
    }

    private fun sendDiscord(level: LogLevel, type: ActionType, player: Player, reason: String) = runBlocking {
        val message = message {
            embed {
                timestamp = Instant.now()
                color = when (level) {
                    INFO -> Color.GREEN
                    WARN -> Color.YELLOW
                    OFF -> Color.BLACK
                }
                author {
                    name = player.name
                    iconUrl = "https://minotar.net/avatar/${player.uniqueId}.png"
                    url = "https://namemc.com/profile/${player.uniqueId}"
                }
                footer {
                    text = "Hawk | Made by NEXTERIAS"
                    iconUrl = "https://github.com/NEXTERIAS.png"
                }
                field {
                    inline = true
                    name = "Type"
                    value = type.name
                }
                field {
                    inline = true
                    name = "Reason"
                    value = reason
                }
            }
        }

        launch {
            Hawk.instance.webhookClient?.send(message)
        }
    }
}