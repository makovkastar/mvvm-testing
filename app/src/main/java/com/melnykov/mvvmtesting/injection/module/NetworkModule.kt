package com.melnykov.mvvmtesting.injection.module

import com.melnykov.mvvmtesting.data.remote.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    @Provides
    fun provideApiService(): ApiService {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://33dca1b7-a837-4a2d-b989-fdf9c6646798.mock.pstmn.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(ApiService::class.java)
    }
}
