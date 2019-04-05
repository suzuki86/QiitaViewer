package com.timersinc.trainning.qiitaviewer

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "title")
    val title: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "user")
    val user: User
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "description")
    val description: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "profile_image_url")
    val imageUrl: String
)
