package com.example.codeexercise1.util.serviceObjects

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "listId") val listId: Int?,
    @field:Json(name = "name") val name: String?
)
