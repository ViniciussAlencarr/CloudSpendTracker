package com.example.cloudspendtracker.data.repository

import android.util.Log
import com.example.cloudspendtracker.data.local.CloudSpendDao
import com.example.cloudspendtracker.data.local.CloudSpendEntity
import com.example.cloudspendtracker.data.remote.CloudSpendApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// O @Inject diz ao Hilt: "Quando alguém pedir esse Repository, construa ele usando essa API e esse DAO"
class CloudSpendRepository @Inject constructor(
    private val api: CloudSpendApi,
    private val dao: CloudSpendDao
) {
    // A UI vai observar isso aqui. Se o banco atualizar, a UI atualiza na hora.
    fun getCloudSpendFlow(): Flow<CloudSpendEntity?> {
        return dao.getCloudSpend()
    }

    // Função que chamaremos em background para sincronizar
    suspend fun syncCloudSpend() {
        try {
            // 1. Busca da AWS
            val remoteData = api.getCloudCosts()

            // 2. Mapeia o DTO da API para a Entidade do Room
            val entity = CloudSpendEntity(
                id = 1, // Fixamos em 1 para sempre sobrescrever com o status do mês atual
                month = remoteData.month,
                totalSpend = remoteData.total_spend,
                budgetLimit = remoteData.budget_limit,
                currency = remoteData.currency,
                services = remoteData.services
            )

            // 3. Salva no banco local
            dao.insertCloudSpend(entity)
            Log.d("CloudSpendRepository", "Dados sincronizados com sucesso!")

        } catch (e: Exception) {
            // Se estiver sem internet, cai aqui. O app não quebra,
            // e a UI continua mostrando os dados que já estão salvos no Room.
            Log.e("CloudSpendRepository", "Erro ao sincronizar com a AWS", e)
        }
    }
}