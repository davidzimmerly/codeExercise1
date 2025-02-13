package com.example.codeexercise1.util.retrofit

import com.example.codeexercise1.util.serviceObjects.Item
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("hiring.json")
     suspend fun getJson(): Response<List<Item>>
}