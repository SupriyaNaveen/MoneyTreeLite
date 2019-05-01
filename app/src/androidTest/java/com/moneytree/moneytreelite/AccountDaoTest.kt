package com.moneytree.moneytreelite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.moneytree.moneytreelite.repository.db.AccountDao
import com.moneytree.moneytreelite.repository.db.AppDatabase
import com.moneytree.moneytreelite.util.Factory
import com.moneytree.moneytreelite.util.LiveDataTestUtil
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AccountDaoTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var accountDao: AccountDao

    @Before
    @Throws(Exception::class)
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        accountDao = appDatabase.accountDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun onFetchingAccounts() {
        val categoryList = LiveDataTestUtil.getValue(accountDao.getAccounts())
        Assert.assertTrue(categoryList.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun onInsertingAllAccounts() {
        val createdDataList = Factory.makeAccountList(10)
        accountDao.insertAll(createdDataList)

        val accountList = LiveDataTestUtil.getValue(accountDao.getAccounts())
        Assert.assertTrue(accountList.isNotEmpty())
        Assert.assertTrue(accountList.size == 10)
    }

    @Test
    @Throws(InterruptedException::class)
    fun onSumOfAllAccounts() {
        val createdDataList = Factory.makeAccountList(10)
        accountDao.insertAll(createdDataList)

        val accountSum = LiveDataTestUtil.getValue(accountDao.getSumOfAccountsBalance())
        Assert.assertTrue(accountSum > 0)
    }
}