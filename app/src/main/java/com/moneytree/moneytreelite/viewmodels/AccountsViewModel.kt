package com.moneytree.moneytreelite.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moneytree.moneytreelite.repository.AccountModel
import com.moneytree.moneytreelite.repository.data.Account
import com.moneytree.moneytreelite.views.adapter.AccountItem
import com.moneytree.moneytreelite.views.adapter.HeaderItem
import com.moneytree.moneytreelite.views.adapter.ListItem
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * View model retrives all information of Account database.
 * In this application AccountActivity is uses this view model.
 * Any activity that requires the account database information can access this view model.
 */
class AccountsViewModel @Inject constructor(private val accountModel: AccountModel) : ViewModel() {

    /**
     * Get live data of accounts information
     */
    suspend fun getAccounts(): LiveData<List<Account>> {
        return accountModel.getAccountsFromDb()
    }

    /**
     * Get the sum of all account balance.
     */
    suspend fun getSumOfAccountsBalance(): LiveData<Float> {
        return accountModel.getSumOfAccountsBalance()
    }

    /**
     * Group by institution. TreeMap sort String alphabetically.
     * Hashmap insertion order not maintained.
     */
    fun groupAccountsByInstitution(accountList: ArrayList<Account>): TreeMap<String, ArrayList<Account>> {

        val groupedHashMap = TreeMap<String, ArrayList<Account>>()

        for (account in accountList) {

            val hashMapKey = account.institution

            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap[hashMapKey]?.add(account)
            } else {
                val list = ArrayList<Account>()
                list.add(account)
                groupedHashMap[hashMapKey!!] = list
            }
        }
        return groupedHashMap
    }

    /**
     * Prepare data for recycler view.
     * Header includes institution and AccountList belongs to header.
     * ListItem defines the type of each row.
     */
    fun prepareListForRecyclerView(groupedHashMap: TreeMap<String, ArrayList<Account>>): ArrayList<ListItem> {
        val consolidatedList = ArrayList<ListItem>()

        for (header in groupedHashMap.keys) {
            val headerItem = HeaderItem()
            headerItem.header = header
            consolidatedList.add(headerItem)

            for (account in groupedHashMap[header]!!) {
                val accountItem = AccountItem()
                accountItem.account = account
                consolidatedList.add(accountItem)
            }
        }
        return consolidatedList
    }
}



