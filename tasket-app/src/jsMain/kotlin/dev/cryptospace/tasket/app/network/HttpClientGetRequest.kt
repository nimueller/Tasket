package dev.cryptospace.tasket.app.network

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.js.json

suspend inline fun <reified T : Any> HttpClient.get(resource: String): HttpResponse<T> {
    val url = "$host$resource"
    return ensureAccessToken { accessToken ->
        sendGetRequest(accessToken, url)
    }
}

suspend inline fun <reified T : Any> sendGetRequest(accessToken: String, url: String): HttpResponse<T> {
    val response = window.fetch(
        url,
        init = object : RequestInit {
            override var headers: dynamic = json("Authorization" to "Bearer $accessToken")
        },
    ).await()

    return HttpResponse(response.status, HttpClient.parseResponse(response))
}
