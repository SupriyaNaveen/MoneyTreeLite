package com.moneytree.moneytreelite.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moneytree.moneytreelite.repository.data.Account
import com.moneytree.moneytreelite.repository.data.DateTypeConverters
import com.moneytree.moneytreelite.repository.data.Transaction

/**
 * The Room database for this app
 */
@Database(entities = [Account::class, Transaction::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao() : TransactionDao
}