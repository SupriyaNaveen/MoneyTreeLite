package com.moneytree.moneytreelite.repository

import androidx.lifecycle.LiveData
import com.moneytree.moneytreelite.repository.data.Transaction
import com.moneytree.moneytreelite.repository.db.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * View Model to which transaction dao is injected.
 */
class TransactionModel @Inject constructor(
    private val transactionDao: TransactionDao
) {

    /**
     * Get all stored transaction from db, which is live data.
     */
    suspend fun getTransactionsFromDb(accountId: Int): LiveData<List<Transaction>> {
        return withContext(Dispatchers.IO) { transactionDao.getTransactionsForAccountId(accountId) }
    }

    /**
     * Get sum of entire transaction for the specified account id.
     */
    suspend fun getTransactionSum(accountId: Int): String {
        return withContext(Dispatchers.IO) {
            transactionDao.getTransactionsSum(accountId)
        }
    }

    suspend fun deleteTransaction(transaction: Transaction?) {
        withContext(Dispatchers.IO) {
            transactionDao.deleteTransaction(transaction)
        }
    }
}