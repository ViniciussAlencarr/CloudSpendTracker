package com.example.cloudspendtracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CloudSpendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCloudSpend(spend: CloudSpendEntity)

    // O retorno em 'Flow' é o grande segredo da reatividade no Android moderno!
    @Query("SELECT * FROM cloud_spend_table WHERE id = 1")
    fun getCloudSpend(): Flow<CloudSpendEntity?>
}