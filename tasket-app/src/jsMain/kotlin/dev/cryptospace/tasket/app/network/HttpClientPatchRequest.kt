package dev.cryptospace.tasket.app.network

import dev.cryptospace.tasket.payloads.RequestPayload
import dev.cryptospace.tasket.payloads.ResponsePayload
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.encodeToString
import org.w3c.fetch.RequestInit
import kotlin.js.json

suspend inline fun <reified REQ, reified RESP> HttpClient.patch(
    resource: String,
    payload: REQ
): HttpResponse<RESP> where REQ : RequestPayload, RESP : ResponsePayload {
    val url = "$host$resource"
    return ensureAccessToken { accessToken ->
        sendPatchRequest(accessToken, url, payload)
    }
}

suspend inline fun <reified REQ, reified RESP> sendPatchRequest(
    accessToken: String,
    url: String,
    payload: REQ
): HttpResponse<RESP> where REQ : RequestPayload, RESP : ResponsePayload {
    val response = window.fetch(
        url,
        init = object : RequestInit {
            override var method: String? = "PATCH"
            override var headers: dynamic = json(
                "Authorization" to "Bearer $accessToken",
                "Content-Type" to "application/json",
            )
            override var body: String? = HttpClient.json.encodeToString(payload)
        },
    ).await()

    return HttpResponse(response.status, HttpClient.parseResponse(response))
}
