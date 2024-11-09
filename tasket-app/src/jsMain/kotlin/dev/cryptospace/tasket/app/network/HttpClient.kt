package dev.cryptospace.tasket.app.network

import dev.cryptospace.tasket.payloads.Payload
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.js.json

object HttpClient {
    val host: String?
        get() = localStorage.getItem("host")

    suspend inline fun <reified T> get(resource: String): T? {
        val url = "$host/$resource"
        console.log("GET $url")
        val response = window.fetch(url).await()
        return parseResponse(response)
    }

    suspend inline fun <reified T : Payload> post(resource: String, payload: T): T? {
        val url = "$host/$resource"
        val body = Json.encodeToString(payload)
        console.log("POST $url")
        console.log("\t$body")
        val response =
            window.fetch(
                url,
                object : RequestInit {
                    override var method: String? = "POST"
                    override var headers: dynamic =
                        json(
                            "Content-Type" to "application/json",
                        )
                    override var body: String? = body
                },
            ).await()
        return parseResponse(response)
    }

    suspend inline fun <reified T> parseResponse(response: Response): T? {
        console.log(response.status)

        return if (response.ok) {
            val responseData = response.text().await()
            console.log("\t$responseData")
            Json.decodeFromString(responseData)
        } else {
            null
        }
    }
}
