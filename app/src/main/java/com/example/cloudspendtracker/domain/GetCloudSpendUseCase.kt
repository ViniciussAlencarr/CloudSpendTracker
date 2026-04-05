package com.example.cloudspendtracker.domain

import com.example.cloudspendtracker.data.local.CloudSpendEntity
import com.example.cloudspendtracker.data.repository.CloudSpendRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// O Hilt também injeta o UseCase automaticamente!
class GetCloudSpendUseCase @Inject constructor(
    private val repository: CloudSpendRepository
) {
    // 1. Aciona a sincronização com a AWS em background
    suspend fun syncData() {
        repository.syncCloudSpend()
    }

    // 2. Entrega o fluxo (Flow) do banco de dados para a ViewModel
    operator fun invoke(): Flow<CloudSpendEntity?> {
        return repository.getCloudSpendFlow()
    }
}