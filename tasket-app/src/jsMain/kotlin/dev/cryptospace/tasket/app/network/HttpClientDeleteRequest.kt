package dev.cryptospace.tasket.app.network

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.js.json

suspend inline fun HttpClient.delete(resource: String): HttpResponse<Unit> {
    val url = "$host$resource"
    return ensureAccessToken { accessToken ->
        sendDeleteRequest(accessToken, url)
    }
}

suspend inline fun sendDeleteRequest(accessToken: String, url: String): HttpResponse<Unit> {
    val response = window.fetch(
        url,
        init = object : RequestInit {
            override var method: String? = "DELETE"
            override var headers: dynamic = json(
                "Authorization" to "Bearer $accessToken",
                "Content-Type" to "application/json",
            )
        },
    ).await()

    return HttpResponse(response.status, null)
}
