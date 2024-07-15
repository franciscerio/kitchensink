package com.fcerio.network.di

import com.fcerio.network.features.BaseApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesBaseApiService(
        retrofit: Retrofit
    ): BaseApiServices = retrofit.create(BaseApiServices::class.java)
}