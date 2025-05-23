package dev.cryptospace.tasket.app.network

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.encodeToString
import org.w3c.fetch.RequestInit
import kotlin.js.json

suspend inline fun <reified T : Any> HttpClient.post(resource: String, payload: T): HttpResponse<T> {
    val url = "$host$resource"
    return ensureAccessToken { accessToken ->
        sendPostRequest(accessToken, url, payload)
    }
}

suspend inline fun <reified T : Any> sendPostRequest(accessToken: String, url: String, payload: T): HttpResponse<T> {
    val response = window.fetch(
        url,
        init = object : RequestInit {
            override var method: String? = "POST"
            override var headers: dynamic = json(
                "Authorization" to "Bearer $accessToken",
                "Content-Type" to "application/json",
            )
            override var body: String? = HttpClient.json.encodeToString(payload)
        },
    ).await()

    return HttpResponse(response.status, HttpClient.parseResponse(response))
}
