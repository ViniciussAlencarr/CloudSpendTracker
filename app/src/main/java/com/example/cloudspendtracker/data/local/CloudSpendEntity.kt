package com.example.cloudspendtracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cloudspendtracker.data.remote.CloudServiceDto

@Entity(tableName = "cloud_spend_table")
data class CloudSpendEntity(
    @PrimaryKey val id: Int = 1, // Usamos ID fixo (1) para sobrescrever sempre com o dado mais recente do mês
    val month: String,
    val totalSpend: Double,
    val budgetLimit: Double,
    val currency: String,
    val services: List<CloudServiceDto> // O Room vai usar o Converter para salvar isso
)