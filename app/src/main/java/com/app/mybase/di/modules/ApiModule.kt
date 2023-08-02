package com.app.mybase.di.modules

import android.app.Application
import com.app.mybase.network.ApiStories
import com.app.mybase.room.UserDao
import com.app.mybase.room.UserDataBase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideUserDao(context: Application): UserDao {
        return UserDataBase.getDatabase(context).userDao()
    }

    @Singleton
    @Provides
    fun getGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideOKHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)

        okHttpClient.addInterceptor(httpLoggingInterceptor)

        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Singleton
    @Provides
    fun getRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://crudcrud.com/api/").client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun provideApiStories(retrofit: Retrofit): ApiStories {
        return retrofit.create(ApiStories::class.java)
    }

}