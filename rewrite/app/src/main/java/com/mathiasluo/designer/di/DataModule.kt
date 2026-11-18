package com.mathiasluo.designer.di

import com.mathiasluo.designer.BuildConfig
import com.mathiasluo.designer.data.fake.FakeShotRepository
import com.mathiasluo.designer.data.remote.DesignerApi
import com.mathiasluo.designer.data.remote.RemoteShotRepository
import com.mathiasluo.designer.domain.repository.ShotRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            },
        )
        .build()

    @Provides
    @Singleton
    fun provideDesignerApi(okHttp: OkHttpClient, json: Json): DesignerApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttp)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(DesignerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideShotRepository(
        fake: FakeShotRepository,
        remote: RemoteShotRepository,
    ): ShotRepository = when (BuildConfig.DATA_SOURCE) {
        "remote", "dribbble" -> remote
        else -> fake
    }
}
