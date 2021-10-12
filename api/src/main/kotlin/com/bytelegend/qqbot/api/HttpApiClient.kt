package com.bytelegend.qqbot.api

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

val OBJECT_MAPPER = ObjectMapper()

class HttpApiClient(private val environment: Environment) {
    private val httpClient = HttpClient.newHttpClient()

    private fun newRequest(url: String) = HttpRequest.newBuilder(URI(url))
        .header("Internal-Api-Secret", environment.apiSecret)
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")

    fun GET(url: String): HttpResponse<String> {
        val request = newRequest(url).GET().build()
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun POST(url: String, payloadJsonObject: Any): HttpResponse<String> {
        val request = newRequest(url)
            .POST(HttpRequest.BodyPublishers.ofString(OBJECT_MAPPER.writeValueAsString(payloadJsonObject)))
            .build()
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }
}
