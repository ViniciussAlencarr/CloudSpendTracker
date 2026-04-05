package com.example.cloudspendtracker.di

import android.app.Application
import androidx.room.Room
import com.example.cloudspendtracker.data.local.CloudSpendDao
import com.example.cloudspendtracker.data.local.CloudSpendDatabase
import com.example.cloudspendtracker.data.remote.CloudSpendApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- CONFIGURAÇÃO DO ROOM (BANCO DE DADOS) ---
    @Provides
    @Singleton
    fun provideDatabase(app: Application): CloudSpendDatabase {
        return Room.databaseBuilder(
            app,
            CloudSpendDatabase::class.java,
            "cloud_spend_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: CloudSpendDatabase): CloudSpendDao {
        return db.dao
    }

    // --- CONFIGURAÇÃO DA AWS E RETROFIT ---
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Log para vermos as requisições no Logcat (ajuda muito a debugar)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Interceptor que injeta sua API Key da AWS automaticamente
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-api-key", "8j0YHMYtAG3fjORtezlXd2Bf4XfbU7vcm73Le1z4") // Substitua pela chave real da AWS
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideCloudSpendApi(client: OkHttpClient): CloudSpendApi {
        return Retrofit.Builder()
            .baseUrl("https://wxboa040ee.execute-api.us-east-2.amazonaws.com/dev/") // Substitua pela sua Invoke URL base
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudSpendApi::class.java)
    }
}