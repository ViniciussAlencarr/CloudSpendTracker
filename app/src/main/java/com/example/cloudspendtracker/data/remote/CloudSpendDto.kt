package com.example.cloudspendtracker.data.remote

data class CloudSpendDto(
    val month: String,
    val total_spend: Double,
    val budget_limit: Double,
    val currency: String,
    val services: List<CloudServiceDto>
)

data class CloudServiceDto(
    val id: Int,
    val name: String,
    val cost: Double,
    val status: String
)