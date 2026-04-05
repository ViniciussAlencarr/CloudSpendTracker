package com.example.cloudspendtracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CloudSpendEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CloudSpendDatabase : RoomDatabase() {
    abstract val dao: CloudSpendDao
}