package com.yempe.financeapps.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockAssetInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return when {
            request.url.encodedPath.contains("currencies") -> {
                createMockResponse(
                    chain,
                    json = """
[
    { "code": "TRY", "name": "Turkish Lira", "symbol": "₺" },
    { "code": "USD", "name": "US Dollar", "symbol": "$" },
    { "code": "EUR", "name": "Euro", "symbol": "€" },
    { "code": "GBP", "name": "Pound", "symbol": "£" },
    { "code": "BTC", "name": "Bitcoin", "symbol": "₿" },
    { "code": "ETH", "name": "Ethereum", "symbol": "Ξ" },
    { "code": "JPY", "name": "Japanese Yen", "symbol": "¥" },
    { "code": "AUD", "name": "Australian Dollar", "symbol": "A$" },
    { "code": "CAD", "name": "Canadian Dollar", "symbol": "C$" },
    { "code": "CHF", "name": "Swiss Franc", "symbol": "CHF" },
    { "code": "CNY", "name": "Chinese Yuan", "symbol": "¥" },
    { "code": "NZD", "name": "New Zealand Dollar", "symbol": "NZ$" },
    { "code": "SEK", "name": "Swedish Krona", "symbol": "kr" },
    { "code": "NOK", "name": "Norwegian Krone", "symbol": "kr" },
    { "code": "SGD", "name": "Singapore Dollar", "symbol": "S$" },
    { "code": "KRW", "name": "South Korean Won", "symbol": "₩" }
]
""".trimIndent()
                )
            }

            else -> chain.proceed(request)
        }
    }

    private fun createMockResponse(chain: Interceptor.Chain, json: String): Response {

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(json.toResponseBody("application/json".toMediaType()))
            .addHeader("content-type", "application/json")
            .build()
    }

}