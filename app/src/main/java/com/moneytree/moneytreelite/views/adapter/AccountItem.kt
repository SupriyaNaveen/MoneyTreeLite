package com.moneytree.moneytreelite.views.adapter

import com.moneytree.moneytreelite.repository.data.Account

class AccountItem : ListItem() {

    var account: Account? = null

    override fun getType(): Int {
        return TYPE_ACCOUNT
    }
}
