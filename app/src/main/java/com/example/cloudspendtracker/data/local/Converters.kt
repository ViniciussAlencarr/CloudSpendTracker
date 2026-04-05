package com.example.cloudspendtracker.data.local

import androidx.room.TypeConverter
import com.example.cloudspendtracker.data.remote.CloudServiceDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromServicesList(value: List<CloudServiceDto>): String {
        val type = object : TypeToken<List<CloudServiceDto>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toServicesList(value: String): List<CloudServiceDto> {
        val type = object : TypeToken<List<CloudServiceDto>>() {}.type
        return gson.fromJson(value, type)
    }

}