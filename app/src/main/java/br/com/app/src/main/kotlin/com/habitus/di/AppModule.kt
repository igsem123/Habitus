package br.com.app.src.main.kotlin.com.habitus.di

import android.app.Application
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule : Application() {

}