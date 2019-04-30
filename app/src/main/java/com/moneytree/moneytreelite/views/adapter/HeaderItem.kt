package com.moneytree.moneytreelite.views.adapter

class HeaderItem : ListItem() {

    var header: String? = null

    override fun getType(): Int {
        return TYPE_HEADER
    }
}