package com.musttool.urlshortenerapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UrlShortenerService {
    @GET("create.php")
    fun shortenUrl(
        @Query("format") format: String,
        @Query("url") url: String
    ): Call<ShortenedUrlResponse>
}
