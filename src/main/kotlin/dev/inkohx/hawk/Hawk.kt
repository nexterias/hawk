package dev.inkohx.hawk

import dev.inkohx.discord.WebhookClient
import dev.inkohx.hawk.action.ActionLogger
import dev.inkohx.hawk.listener.BlockProtectionListener
import dev.inkohx.hawk.listener.CommandProtectionListener
import dev.inkohx.hawk.listener.PlayerEventListener
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Hawk : JavaPlugin() {
    var webhookClient: WebhookClient? = null

    val actionLogger = ActionLogger()

    init {
        instance = this
    }

    override fun onLoad() {
        saveDefaultConfig()
        setupWebhookClient()
    }

    override fun onEnable() {
        registerEventListeners()
    }

    private fun registerEventListeners() {
        val listeners = mutableListOf<Listener>(
            PlayerEventListener()
        )

        if (config.getBoolean("commandProtection.enable")) listeners.add(CommandProtectionListener())
        if (config.getBoolean("blockProtection.enable")) listeners.add(BlockProtectionListener())

        listeners.forEach { server.pluginManager.registerEvents(it, this) }
    }

    private fun setupWebhookClient() {
        if (!config.getBoolean("logging.discord.enable")) return logger.warning("Discordへの通知は\"config.yml\"で無効に設定されています。")

        val token = config.getString("logging.discord.webhook.token")
        val id = config.getString("logging.discord.webhook.id")

        if (token == null) return logger.warning("Webhookのトークンが設定されていません。")
        if (id == null) return logger.warning("WebhookのIDが入力されていません。")

        webhookClient = WebhookClient(id, token)

        logger.info("Discord Webhook連携機能は有効です。")
    }

    companion object {
        @JvmStatic
        lateinit var instance: Hawk
    }
}
