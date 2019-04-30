package com.moneytree.moneytreelite.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moneytree.moneytreelite.repository.data.Account

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts ORDER BY account_name ASC")
    fun getAccounts(): LiveData<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(accountList: List<Account>)

    @Query("SELECT SUM(current_balance) FROM accounts")
    fun getSumOfAccountsBalance(): LiveData<Float>
}
