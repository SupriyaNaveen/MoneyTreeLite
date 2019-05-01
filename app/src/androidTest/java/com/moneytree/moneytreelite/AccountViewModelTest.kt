package com.moneytree.moneytreelite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.moneytree.moneytreelite.repository.AccountModel
import com.moneytree.moneytreelite.repository.data.Account
import com.moneytree.moneytreelite.repository.db.AppDatabase
import com.moneytree.moneytreelite.util.Factory
import com.moneytree.moneytreelite.util.LiveDataTestUtil
import com.moneytree.moneytreelite.viewmodels.AccountsViewModel
import kotlinx.coroutines.runBlocking
import org.junit.*

class AccountViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: AccountsViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        val accountModel = AccountModel(appDatabase.accountDao())
        viewModel = AccountsViewModel(accountModel)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun onGetAccounts() {
        val dataList = LiveDataTestUtil.getValue(runBlocking { viewModel.getAccounts() })
        Assert.assertTrue(dataList.isEmpty())

        val createdDataList = Factory.makeAccountList(10)
        appDatabase.accountDao().insertAll(createdDataList)

        val accountList = LiveDataTestUtil.getValue(runBlocking { viewModel.getAccounts() })
        Assert.assertTrue(accountList.isNotEmpty())
        Assert.assertTrue(accountList.size == 10)
    }

    @Test
    @Throws(InterruptedException::class)
    fun onGetAccountSum() {
        val createdDataList = Factory.makeAccountList(10)
        appDatabase.accountDao().insertAll(createdDataList)

        val dataSum = LiveDataTestUtil.getValue(runBlocking { viewModel.getSumOfAccountsBalance() })
        Assert.assertTrue(dataSum > 0)
    }

    @Test
    fun onGroupAccountsByInstitutionName() {
        val createdDataList = ArrayList<Account>()
        createdDataList.add(Factory.makeAccountForInstitution("Institution B"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution B"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution B"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution A"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution A"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution A"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution A"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution C"))

        val resultTreeMapList = viewModel.groupAccountsByInstitution(createdDataList)
        Assert.assertTrue(resultTreeMapList.size == 3)
        Assert.assertTrue(resultTreeMapList.firstKey() == "Institution A")
    }

    @Test
    fun onPrepareListForRecyclerView() {
        val createdDataList = ArrayList<Account>()
        createdDataList.add(Factory.makeAccountForInstitution("Institution B"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution B"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution B"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution A"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution A"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution A"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution A"))
        createdDataList.add(Factory.makeAccountForInstitution("Institution C"))

        val resultTreeMapList = viewModel.groupAccountsByInstitution(createdDataList)
        val resultListForUi = viewModel.prepareListForRecyclerView(resultTreeMapList)
        Assert.assertTrue(resultListForUi.size == (createdDataList.size + resultTreeMapList.keys.size))
    }

}
