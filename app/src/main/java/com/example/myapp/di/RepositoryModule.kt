package com.example.myapp.di

import com.example.data.DaoRepositoryImpl
import com.example.data.RepositoryImpl
import com.example.data.SharedPreferencesServiceImpl
import com.example.domain.DaoRepository
import com.example.domain.Repository
import com.example.domain.SharedPreferencesService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

    @Binds
    abstract fun bindDaoRepository(daoRepositoryImpl: DaoRepositoryImpl): DaoRepository

    @Binds
    abstract fun bindSharedPreferencesService(sharedPreferencesServiceImpl: SharedPreferencesServiceImpl): SharedPreferencesService
}
