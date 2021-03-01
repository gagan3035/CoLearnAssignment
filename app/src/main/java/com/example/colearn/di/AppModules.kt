package com.example.colearn.di

import com.example.colearn.network.ImageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.modules.ApplicationContextModule
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Gagan on 27/02/21.
 */

@InstallIn(SingletonComponent::class)
@Module
class AppModules {

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient().newBuilder().addInterceptor(httpLoggingInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideHttpLogginInterceptor(): HttpLoggingInterceptor {
        var interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
    @Singleton
    @Provides
    fun provideGsonConvertorFactory(): GsonConverterFactory {

        return GsonConverterFactory.create()

    }
    @Singleton
    @Provides
    fun provideRetroFitClient(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory) : Retrofit {

        return Retrofit.Builder().baseUrl("https://api.unsplash.com/").client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideLoginService(retrofit: Retrofit):ImageService{
        return retrofit.create(ImageService::class.java)
    }

}