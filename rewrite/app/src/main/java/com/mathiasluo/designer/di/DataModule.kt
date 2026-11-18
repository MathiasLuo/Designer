package com.mathiasluo.designer.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mathiasluo.designer.BuildConfig
import com.mathiasluo.designer.data.ShotRemoteDataSource
import com.mathiasluo.designer.data.fake.FakeShotRemoteDataSource
import com.mathiasluo.designer.data.local.DesignerDatabase
import com.mathiasluo.designer.data.local.ShotDao
import com.mathiasluo.designer.data.remote.ApiShotRemoteDataSource
import com.mathiasluo.designer.data.remote.DesignerApi
import com.mathiasluo.designer.data.repository.CachedShotRepository
import com.mathiasluo.designer.domain.repository.ShotRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideDatabase(@ApplicationContext context: Context): DesignerDatabase =
        Room.databaseBuilder(context, DesignerDatabase::class.java, "designer.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideShotDao(database: DesignerDatabase): ShotDao = database.shotDao()

    @Provides
    @Singleton
    fun provideShotRemoteDataSource(
        fake: FakeShotRemoteDataSource,
        api: ApiShotRemoteDataSource,
    ): ShotRemoteDataSource = when (BuildConfig.DATA_SOURCE) {
        "remote", "dribbble" -> api
        else -> fake
    }

    @Provides
    @Singleton
    fun provideShotRepository(repository: CachedShotRepository): ShotRepository = repository
}
