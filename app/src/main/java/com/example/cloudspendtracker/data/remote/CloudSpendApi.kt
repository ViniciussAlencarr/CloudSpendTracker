package com.example.cloudspendtracker.data.remote

import retrofit2.http.GET
import retrofit2.http.Header

interface CloudSpendApi {
    @GET("costs") // A rota que configuramos no API Gateway
    suspend fun getCloudCosts(): CloudSpendDto
}