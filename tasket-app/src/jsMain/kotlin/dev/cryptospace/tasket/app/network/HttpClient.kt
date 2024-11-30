package dev.cryptospace.tasket.app.network

import dev.cryptospace.tasket.payloads.authentication.LoginRequestPayload
import dev.cryptospace.tasket.payloads.authentication.LoginResponsePayload
import dev.cryptospace.tasket.payloads.authentication.RefreshTokenRequestPayload
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
    private var accessToken: String
        get() = localStorage.getItem("accessToken") ?: ""
        set(value) = localStorage.setItem("accessToken", value)
    private var refreshToken: String
        get() = localStorage.getItem("refreshToken") ?: ""
        set(value) = localStorage.setItem("refreshToken", value)

    suspend fun HttpClient.login(payload: LoginRequestPayload): Boolean {
        val url = "$host/rest/login"
        val body = Json.encodeToString(payload)
        return refreshTokens(url, body).parsedEntity?.let { responsePayload ->
            setTokens(responsePayload)
            true
        } ?: false
    }

    private suspend fun HttpClient.refreshTokens(url: String, body: String): HttpResponse<LoginResponsePayload> {
        val response = window.fetch(
            url,
            object : RequestInit {
                override var method: String? = "POST"
                override var headers: dynamic = json("Content-Type" to "application/json")
                override var body: String? = body
            },
        ).await()
        val parsedResponse = parseResponse<LoginResponsePayload>(response)
        return HttpResponse(response.status, parsedResponse)
    }

    private fun setTokens(responsePayload: LoginResponsePayload) {
        accessToken = responsePayload.accessToken
        refreshToken = responsePayload.refreshToken
        println("Access token and refresh token refreshed")
    }

    suspend fun <T : Any> ensureAccessToken(sender: suspend (String) -> HttpResponse<T>): HttpResponse<T> {
        val response = sender(accessToken)

        // try to refresh the access token if the request was unauthorized,
        // if we are logged in (refreshToken is not blank)
        if (refreshToken.isNotBlank() && response.status == 401.toShort()) {
            refreshAccessToken(refreshToken)?.let { responsePayload ->
                setTokens(responsePayload)
            }

            return sender(accessToken)
        }

        return response
    }

    private suspend fun HttpClient.refreshAccessToken(refreshToken: String): LoginResponsePayload? {
        val url = "$host/rest/refresh"
        val body = Json.encodeToString(RefreshTokenRequestPayload(refreshToken))
        return refreshTokens(url, body).parsedEntity
    }

    suspend inline fun <reified T> parseResponse(response: Response): T? {
        return if (response.ok) {
            val responseData = response.text().await()
            Json.decodeFromString(responseData)
        } else {
            null
        }
    }
}
