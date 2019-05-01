package com.moneytree.moneytreelite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.moneytree.moneytreelite.repository.TransactionModel
import com.moneytree.moneytreelite.repository.db.AppDatabase
import com.moneytree.moneytreelite.util.Factory
import com.moneytree.moneytreelite.util.LiveDataTestUtil
import com.moneytree.moneytreelite.viewmodels.TransactionViewModel
import kotlinx.coroutines.runBlocking
import org.junit.*

class TransactionViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: TransactionViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        val transactionModel = TransactionModel(appDatabase.transactionDao())
        viewModel = TransactionViewModel(transactionModel)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun onGetTransaction() {
        val dataList = LiveDataTestUtil.getValue(runBlocking { viewModel.getTransactions(10) })
        Assert.assertTrue(dataList.isEmpty())

        val createdDataList = Factory.makeTransactionList(10, 20)
        appDatabase.transactionDao().insertAll(createdDataList)

        val accountList = LiveDataTestUtil.getValue(runBlocking { viewModel.getTransactions(20) })
        Assert.assertTrue(accountList.isNotEmpty())
        Assert.assertTrue(accountList.size == createdDataList.size)
    }

    @Test
    @Throws(InterruptedException::class)
    fun onGetTransactionSum() {
        val createdDataList = Factory.makeTransactionList(10, 20)
        appDatabase.transactionDao().insertAll(createdDataList)

        val dataSum = runBlocking { viewModel.getTransactionSum(20) }
        Assert.assertTrue(Integer.parseInt(dataSum) > 0)
    }

    @Test
    @Throws(InterruptedException::class)
    fun onDeleteTransaction() {
        val createdDataList = Factory.makeTransactionList(10, 20)
        appDatabase.transactionDao().insertAll(createdDataList)

        runBlocking { viewModel.deleteTransaction(createdDataList[0]) }
        val afterDeleteDataList = LiveDataTestUtil.getValue(runBlocking { viewModel.getTransactions(20) })
        Assert.assertTrue(afterDeleteDataList.size == createdDataList.size - 1)
    }

    @Test
    fun onGroupTransactionByMonth() {
        val createdDataList = Factory.makeTransactionList(10, 20)
        val resultLinkedMapList = viewModel.groupTransactionByMonth(ArrayList(createdDataList))
        Assert.assertTrue(resultLinkedMapList.size > 0)
        //Current date added so only current month.
        Assert.assertTrue(resultLinkedMapList.size == 1)
    }

    @Test
    fun onPrepareListForRecyclerView() {
        val createdDataList = Factory.makeTransactionList(10, 20)
        val resultLinkedMapList = viewModel.groupTransactionByMonth(ArrayList(createdDataList))
        val resultListForUi = viewModel.prepareListForRecyclerView(resultLinkedMapList)
        Assert.assertTrue(resultListForUi.size == (createdDataList.size + resultLinkedMapList.keys.size))
    }

}
