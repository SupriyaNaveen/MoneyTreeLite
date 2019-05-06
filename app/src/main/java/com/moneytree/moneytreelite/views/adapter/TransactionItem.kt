package com.moneytree.moneytreelite.views.adapter

import com.moneytree.moneytreelite.repository.data.Transaction

class TransactionItem : ListItem() {

    var transaction: Transaction? = null

    override fun getType() = TYPE_TRANSACTION
}
