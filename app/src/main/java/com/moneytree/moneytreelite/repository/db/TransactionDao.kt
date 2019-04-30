package com.moneytree.moneytreelite.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moneytree.moneytreelite.repository.data.Transaction

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY date ASC")
    fun getTransactions(): LiveData<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(transactionList: List<Transaction>)

    @Query("SELECT * FROM transactions WHERE account_id=:accountId ORDER BY date DESC")
    fun getTransactionsForAccountId(accountId: Int): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE account_id=:accountId")
    fun getTransactionsSum(accountId: Int): String

    @Delete
    fun deleteTransaction(transaction: Transaction?)
}