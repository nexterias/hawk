package dev.inkohx.hawk.listener

import dev.inkohx.hawk.Hawk
import dev.inkohx.hawk.action.ActionType
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class BlockProtectionListener : Listener {
    private val canRemoveFromInventory get() = Hawk.instance.config.getBoolean("blockProtection.removeFromInventory")
    private val bannedBlocks
        get() = Hawk.instance.config.getStringList("blockProtection.blocks").map { Material.valueOf(it) }

    @EventHandler
    fun onPlaceBlock(event: BlockPlaceEvent) {
        if (!bannedBlocks.contains(event.block.type)) return
        if (canRemoveFromInventory) event.player.inventory.remove(event.block.type)

        val blockName = event.block.type.name

        event.isCancelled = true
        event.player.spigot().sendMessage(
            TextComponent().apply {
                text = "${blockName}は配置することができません"
                color = ChatColor.RED
            }
        )

        Hawk.instance.actionLogger.log(ActionType.PLACE_BANNED_BLOCK, event.player, "${blockName}を配置しようとしました。")
    }

    @EventHandler
    fun onBreakBlock(event: BlockBreakEvent) {
        if (!bannedBlocks.contains(event.block.type)) return

        val blockName = event.block.type.name

        event.isCancelled = true
        event.player.spigot().sendMessage(
            TextComponent().apply {
                text = "${blockName}を破壊することはできません。"
                color = ChatColor.RED
            }
        )

        Hawk.instance.actionLogger.log(ActionType.BREAK_BANNED_BLOCK, event.player, "${blockName}を破壊しようとしました。")
    }
}