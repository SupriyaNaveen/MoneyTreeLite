package com.moneytree.moneytreelite.repository

import androidx.lifecycle.LiveData
import com.moneytree.moneytreelite.repository.data.Account
import com.moneytree.moneytreelite.repository.db.AccountDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * View Model to which account dao is injected.
 */
class AccountModel @Inject constructor(
    private val accountDao: AccountDao
) {

    /**
     * Get all stored accounts from db, which is live data.
     */
    suspend fun getAccountsFromDb(): LiveData<List<Account>> {
        return withContext(Dispatchers.IO) { accountDao.getAccounts() }
    }

    /**
     * Sum of all accounts balance
     */
    suspend fun getSumOfAccountsBalance(): LiveData<Float> {
        return withContext(Dispatchers.IO) {
            accountDao.getSumOfAccountsBalance()
        }
    }
}