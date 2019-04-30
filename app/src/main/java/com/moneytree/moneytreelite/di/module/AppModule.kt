package com.moneytree.moneytreelite.di.module

import android.app.Application
import androidx.room.Room
import com.moneytree.moneytreelite.repository.db.AccountDao
import com.moneytree.moneytreelite.repository.db.AppDatabase
import com.moneytree.moneytreelite.repository.db.TransactionDao
import com.moneytree.moneytreelite.utility.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * class annotated with @Module, which will receive the application instance via constructor,
 * store it in a property, and return it using a method annotated with @Provides @Singleton
 * App related instance provider.
 */
@Module
class AppModule(val app: Application) {

//    companion object {
//        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                 Change table when version changes
//            }
//        }
//    }

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideAccountsDatabase(app: Application): AppDatabase = Room.databaseBuilder(
        app,
        AppDatabase::class.java, Constants.DATABASE_NAME
    )
        /*.addMigrations(MIGRATION_1_2)*/
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideAccountsDao(
        database: AppDatabase
    ): AccountDao = database.accountDao()

    @Provides
    @Singleton
    fun provideTransactionDao(
        database: AppDatabase
    ): TransactionDao = database.transactionDao()
}