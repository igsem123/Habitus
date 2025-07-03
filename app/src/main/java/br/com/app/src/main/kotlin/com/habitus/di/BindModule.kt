package br.com.app.src.main.kotlin.com.habitus.di

import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitLogRepository
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitLogRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {

    @Binds
    @Singleton
    abstract fun bindHabitLogRepository(
        impl: HabitLogRepositoryImpl
    ): HabitLogRepository
}
