package com.example.rubikanschallenge.di



import android.app.Application
import androidx.room.Room
import com.example.rubikanschallenge.data.local.UsersDataBase
import com.example.rubikanschallenge.data.network.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(UserApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)


    @Provides
    @Singleton
    fun provideDatabase(app: Application) : UsersDataBase =
        Room.databaseBuilder(app, UsersDataBase::class.java, "users_database")
            .build()

}