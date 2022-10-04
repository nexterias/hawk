package dev.inkohx.discord

import dev.inkohx.discord.component.Message
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WebhookClient(private val id: String, private val token: String) {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun send(message: Message) = withContext(Dispatchers.IO) {
        httpClient.post("https://discord.com/api/v10/webhooks/$id/$token") {
            setBody(message)
            contentType(ContentType.Application.Json)
        }
    }
}