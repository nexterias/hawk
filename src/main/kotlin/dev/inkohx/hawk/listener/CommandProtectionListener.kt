package dev.inkohx.hawk.listener

import dev.inkohx.hawk.Hawk
import dev.inkohx.hawk.action.ActionType
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandProtectionListener : Listener {
    @EventHandler(ignoreCancelled = false)
    fun onPreExecuteCommand(event: PlayerCommandPreprocessEvent) {
        val bannedCommands = Hawk.instance.config.getStringList("commandProtection.commands")
        val bannedCommand = bannedCommands.find { event.message.startsWith("/$it") } ?: return

        event.isCancelled = true
        event.player.spigot().sendMessage(
            TextComponent().apply {
                text = "${bannedCommand}は使用できません。"
                color = ChatColor.RED
            }
        )

        Hawk.instance.actionLogger.log(ActionType.USE_BANNED_COMMAND, event.player, "${bannedCommand}を使用")
    }
}