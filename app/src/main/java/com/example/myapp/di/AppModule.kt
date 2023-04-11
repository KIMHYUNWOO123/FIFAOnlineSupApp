package com.example.myapp.di

import com.example.data.ApiService
import com.example.data.MetaApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException
import javax.inject.Singleton


@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun baseUrl(): String = "https://api.nexon.co.kr/fifaonline4/v1.0/"

    @Provides
    @Singleton
    fun metaBaseUrl(): String = "https://static.api.nexon.co.kr/fifaonline4/latest/"

    @Provides
    @Singleton
    fun provideApiService(baseUrl: String, gson: Gson, client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMetaApiService(metaBaseUrl: String, gson: Gson): MetaApiService {
        return Retrofit.Builder()
            .baseUrl(metaBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MetaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttp(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            with(chain) {
                val newRequest = request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJYLUFwcC1SYXRlLUxpbWl0IjoiNTAwOjEwIiwiYWNjb3VudF9pZCI6IjEwMDkzOTM5OCIsImF1dGhfaWQiOiIyIiwiZXhwIjoxNjk2NDE0MDU2LCJpYXQiOjE2ODA4NjIwNTYsIm5iZiI6MTY4MDg2MjA1Niwic2VydmljZV9pZCI6IjQzMDAxMTQ4MSIsInRva2VuX3R5cGUiOiJBY2Nlc3NUb2tlbiJ9.OHXmAGwJIMg40EA_0DXprwMCtmTM35VQUqlT1Di9hJw"
                    )
                    .build()
                proceed(newRequest)
            }
        }
    }
}
