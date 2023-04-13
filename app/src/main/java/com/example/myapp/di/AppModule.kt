package com.example.myapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.data.ApiService
import com.example.data.MetaApiService
import com.example.data.db.DivisionDatabase
import com.example.data.db.MatchTypeDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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
    @Named("baseUrl")
    fun baseUrl(): String = "https://api.nexon.co.kr/fifaonline4/v1.0/"

    @Provides
    @Singleton
    @Named("metaBaseUrl")
    fun metaBaseUrl(): String = "https://static.api.nexon.co.kr/fifaonline4/latest/"

    @Provides
    @Singleton
    @Named("api")
    fun provideApiService(@Named("baseUrl") baseUrl: String, gson: Gson, client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("meta")
    fun provideMetaApiService(@Named("metaBaseUrl") metaBaseUrl: String, gson: Gson): MetaApiService {
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

    @Provides
    @Singleton
    fun provideMatchDatabase(@ApplicationContext context: Context): MatchTypeDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            MatchTypeDatabase::class.java,
            "MatchTypeDatabase"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDivisionDatabase(@ApplicationContext context: Context): DivisionDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            DivisionDatabase::class.java,
            "DivisionDatabase"
        ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_MATCH_TIME, Context.MODE_PRIVATE)
    }

    companion object {
        const val PREF_MATCH_TIME = "PREF_FIFA"
    }
}
