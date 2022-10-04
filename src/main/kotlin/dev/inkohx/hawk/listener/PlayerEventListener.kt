package dev.inkohx.hawk.listener

import dev.inkohx.hawk.Hawk
import dev.inkohx.hawk.action.ActionType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerGameModeChangeEvent

class PlayerEventListener : Listener {
    @EventHandler
    fun onChangeGameMode(event: PlayerGameModeChangeEvent) =
        Hawk.instance.actionLogger.log(ActionType.CHANGE_GAME_MODE, event.player, "${event.newGameMode.name}に変更")
}