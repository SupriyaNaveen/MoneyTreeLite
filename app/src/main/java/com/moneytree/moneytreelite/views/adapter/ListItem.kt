package com.moneytree.moneytreelite.views.adapter

abstract class ListItem {

    abstract fun getType() : Int

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ACCOUNT = 1
        const val TYPE_TRANSACTION = 2
    }
}
