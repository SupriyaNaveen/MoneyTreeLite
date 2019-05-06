package com.moneytree.moneytreelite.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moneytree.moneytreelite.repository.TransactionModel
import com.moneytree.moneytreelite.repository.data.Transaction
import com.moneytree.moneytreelite.utility.getDateInFormat
import com.moneytree.moneytreelite.views.adapter.HeaderItem
import com.moneytree.moneytreelite.views.adapter.ListItem
import com.moneytree.moneytreelite.views.adapter.TransactionItem
import javax.inject.Inject

/**
 * View model retrives all information of Transaction database.
 * In this application TransactionActivity is uses this view model.
 * Any activity that requires the transaction database information can access this view model.
 */
class TransactionViewModel @Inject constructor(private val transactionModel: TransactionModel) : ViewModel() {

    /**
     * Get the transaction based on account id. Live data of Transaction handled in activity
     */
    suspend fun getTransactions(accountId: Int): LiveData<List<Transaction>> {
        return transactionModel.getTransactionsFromDb(accountId)
    }

    /**
     * Date wise ordered in query itself.
     * Linked hash map used to maintain the insertion order.
     */
    fun groupTransactionByMonth(transactionList: ArrayList<Transaction>): LinkedHashMap<String, ArrayList<Transaction>> {
        val groupedHashMap = LinkedHashMap<String, ArrayList<Transaction>>()

        for (transaction in transactionList) {

            val hashMapKey = getDateInFormat("MMMM YYYY", transaction.date!!)

            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap[hashMapKey]?.add(transaction)
            } else {
                val list = ArrayList<Transaction>()
                list.add(transaction)
                groupedHashMap[hashMapKey] = list
            }
        }
        return groupedHashMap
    }

    /**
     * Prepare data for recycler view.
     * Header includes month and TransactionList belongs to header.
     * ListItem defines the type of each row.
     */
    fun prepareListForRecyclerView(groupedHashMap: LinkedHashMap<String, ArrayList<Transaction>>): ArrayList<ListItem> {
        val consolidatedList = ArrayList<ListItem>()

        for (header in groupedHashMap.keys) {
            val headerItem = HeaderItem()

            //Add credit debit text
            headerItem.header = header + getCreditDebitText(groupedHashMap[header])
            consolidatedList.add(headerItem)

            for (transaction in groupedHashMap[header]!!) {
                val transactionItem = TransactionItem()
                transactionItem.transaction = transaction
                consolidatedList.add(transactionItem)
            }
        }
        return consolidatedList
    }

    /**
     * Calculate debit and credit balance.
     */
    private fun getCreditDebitText(transactionList: ArrayList<Transaction>?): String {
        var creditBalance = 0.0
        var debitBalance = 0.0
        for (transaction in transactionList!!) {
            if (transaction.amount > 0)
                creditBalance += transaction.amount
            else debitBalance += transaction.amount
        }
        return "  +$creditBalance $debitBalance"
    }

    suspend fun getTransactionSum(accountId: Int): String {
        return transactionModel.getTransactionSum(accountId)
    }

    suspend fun deleteTransaction(transaction: Transaction?) {
        transactionModel.deleteTransaction(transaction)
    }

}



