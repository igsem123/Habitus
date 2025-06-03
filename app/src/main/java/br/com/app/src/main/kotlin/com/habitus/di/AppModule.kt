package br.com.app.src.main.kotlin.com.habitus.di

import android.app.Application
import androidx.room.Room
import br.com.app.src.main.kotlin.com.habitus.data.database.HabitusDatabase
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitRepository
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule : Application() {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): HabitusDatabase {
        return Room.databaseBuilder(
            application,
            HabitusDatabase::class.java,
            "habitus_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHabitRepository(database: HabitusDatabase): HabitRepository {
        return HabitRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth =
        FirebaseAuth.getInstance()
}