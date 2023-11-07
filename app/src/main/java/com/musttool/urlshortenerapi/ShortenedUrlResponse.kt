package com.musttool.urlshortenerapi

import com.google.gson.annotations.SerializedName

data class ShortenedUrlResponse(
    @SerializedName("shorturl") val shortUrl: String
)
