package com.melnykov.mvvmtesting.injection.module

import dagger.Module
import dagger.Provides
import com.melnykov.mvvmtesting.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    @Provides
    fun provideApiService(): ApiService {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(ApiService::class.java)
    }
}
