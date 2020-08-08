package com.and1ss.android.photogallery.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "c13cd00ce0ecfc62f57549df219a1da1"

class PhotoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url().newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("extras", "url_s, url_o")
            .addQueryParameter("safe_search ", "3")
            .build()

        val newRequest = request.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}