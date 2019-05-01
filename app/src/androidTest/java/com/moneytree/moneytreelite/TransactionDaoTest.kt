package com.moneytree.moneytreelite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.moneytree.moneytreelite.repository.db.AppDatabase
import com.moneytree.moneytreelite.repository.db.TransactionDao
import com.moneytree.moneytreelite.util.Factory
import com.moneytree.moneytreelite.util.LiveDataTestUtil
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var transactionDao: TransactionDao

    @Before
    @Throws(Exception::class)
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        transactionDao = appDatabase.transactionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun onFetchingTransactions() {
        val dataList = LiveDataTestUtil.getValue(transactionDao.getTransactions())
        Assert.assertTrue(dataList.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun onInsertingAllTransactions() {
        val createdDataList = Factory.makeTransactionList(10)
        transactionDao.insertAll(createdDataList)

        val dataList = LiveDataTestUtil.getValue(transactionDao.getTransactions())
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertTrue(dataList.size == createdDataList.size)
    }

    @Test
    @Throws(InterruptedException::class)
    fun fetchTransactionForAccountId() {
        val createdDataList = Factory.makeTransactionList(10, 1)
        transactionDao.insertAll(createdDataList)

        val dataList = LiveDataTestUtil.getValue(transactionDao.getTransactionsForAccountId(1))
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertTrue(dataList.size == createdDataList.size)
    }

    @Test
    @Throws(InterruptedException::class)
    fun onSumOfAllTransaction() {
        val createdDataList = Factory.makeTransactionList(10, 1)
        transactionDao.insertAll(createdDataList)

        val balanceSum = transactionDao.getTransactionsSum(1)
        Assert.assertTrue(Integer.parseInt(balanceSum) > 0)
    }

    @Test
    @Throws(InterruptedException::class)
    fun onDeleteTransaction() {
        val createdDataList = Factory.makeTransactionList(10)
        transactionDao.insertAll(createdDataList)

        val dataList = LiveDataTestUtil.getValue(transactionDao.getTransactions())
        transactionDao.deleteTransaction(dataList[0])

        val dataAfterDeleteList = LiveDataTestUtil.getValue(transactionDao.getTransactions())
        Assert.assertTrue(dataAfterDeleteList.size == dataList.size - 1)
    }
}