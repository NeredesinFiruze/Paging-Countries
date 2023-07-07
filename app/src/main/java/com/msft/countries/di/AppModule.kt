package com.msft.countries.di

import com.msft.countries.data.remote.CountriesService
import com.msft.countries.data.repository.CountriesRepository
import com.msft.countries.data.repository.CountriesRepositoryImpl
import com.msft.countries.util.Constants.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindCountriesRepository(impl: CountriesRepositoryImpl): CountriesRepository

    companion object{
        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Provides
        fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

        @Provides
        fun provideHttpInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor{}.setLevel(HttpLoggingInterceptor.Level.BODY)

        @Provides
        fun providesCountriesService(retrofit: Retrofit): CountriesService =
            retrofit.create(CountriesService::class.java)
    }
}