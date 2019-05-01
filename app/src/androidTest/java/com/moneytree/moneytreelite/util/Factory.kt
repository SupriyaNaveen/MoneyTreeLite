package com.moneytree.moneytreelite.util

import com.moneytree.moneytreelite.repository.data.Account
import com.moneytree.moneytreelite.repository.data.Transaction
import com.moneytree.moneytreelite.util.DataFactory.Factory.randomInt
import com.moneytree.moneytreelite.util.DataFactory.Factory.randomUuid
import java.util.*

class Factory {

    companion object Factory {

        private fun makeAccount(): Account {
            return Account(
                randomInt(),
                randomUuid(),
                randomUuid(),
                randomUuid(),
                randomInt().toFloat(),
                randomInt().toFloat()
            )
        }

        fun makeAccountForInstitution(institutionName: String): Account {
            return Account(
                randomInt(),
                randomUuid(),
                institutionName,
                randomUuid(),
                randomInt().toFloat(),
                randomInt().toFloat()
            )
        }

        fun makeAccountList(count: Int): List<Account> {
            val categoryList = mutableListOf<Account>()
            repeat(count) {
                categoryList.add(makeAccount())
            }
            return categoryList
        }

        fun makeTransaction(): Transaction {
            return Transaction(
                randomInt(),
                randomInt(),
                randomUuid(),
                Calendar.getInstance().time,
                randomInt().toFloat(),
                randomUuid()
            )
        }

        private fun makeTransaction(accountId: Int): Transaction {
            return Transaction(
                randomInt(),
                accountId,
                randomUuid(),
                Calendar.getInstance().time,
                randomInt().toFloat(),
                randomUuid()
            )
        }


        fun makeTransactionList(count: Int, accountId: Int): List<Transaction> {
            val categoryList = mutableListOf<Transaction>()
            repeat(count) {
                categoryList.add(makeTransaction(accountId))
            }
            return categoryList
        }

        fun makeTransactionList(count: Int): List<Transaction> {
            val categoryList = mutableListOf<Transaction>()
            repeat(count) {
                categoryList.add(makeTransaction())
            }
            return categoryList
        }
    }
}