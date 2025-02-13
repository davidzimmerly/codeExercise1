package com.example.codeexercise1.di

import android.content.Context
import androidx.room.Room
import com.example.codeexercise1.db.AppDatabase
import com.example.codeexercise1.db.ItemsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideItemsDao(database: AppDatabase): ItemsDao {
        return database.itemsDao()
    }
}
