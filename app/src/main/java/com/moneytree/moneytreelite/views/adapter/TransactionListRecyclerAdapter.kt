package com.moneytree.moneytreelite.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.moneytreelite.R
import com.moneytree.moneytreelite.utility.getDateInFormat

class TransactionListRecyclerAdapter(consolidatedList: List<ListItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var consolidatedList: List<ListItem> = ArrayList()

    init {
        this.consolidatedList = consolidatedList
    }

    override fun getItemViewType(position: Int) = consolidatedList[position].getType()

    override fun getItemCount() = consolidatedList.size


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {

            ListItem.TYPE_TRANSACTION -> {
                val v1 = inflater.inflate(
                    R.layout.layout_transaction_row, parent,
                    false
                )
                viewHolder = TransactionViewHolder(v1)
            }

            ListItem.TYPE_HEADER -> {
                val v2 = inflater.inflate(R.layout.layout_account_header, parent, false)
                viewHolder = HeaderViewHolder(v2)
            }
        }

        return viewHolder!!
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {

        when (viewHolder.itemViewType) {

            ListItem.TYPE_TRANSACTION -> {
                val transactionItem = consolidatedList[position] as TransactionItem
                val transactionViewHolder = viewHolder as TransactionViewHolder

                transactionItem.transaction?.run {
                    transactionViewHolder.mTextViewTransactionDate.text = date?.let { getDateInFormat("dd", it) }
                    transactionViewHolder.mTextViewTransactionDescription.text = description.toString()
                    transactionViewHolder.mTextViewTransactionBalance.text = amount.toString()
                }
            }

            ListItem.TYPE_HEADER -> {
                val headerItem = consolidatedList[position] as HeaderItem
                val headerViewHolder = viewHolder as HeaderViewHolder
                // Populate header item data here
                headerViewHolder.mHeaderText.text = headerItem.header
            }
        }
    }

    fun updateTransactionData(consolidatedList: ArrayList<ListItem>) {
        this.consolidatedList = consolidatedList
        notifyDataSetChanged()
    }

    fun getTransactionData() = consolidatedList

    // ViewHolder for header row item
    internal inner class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mHeaderText: TextView = v.findViewById(R.id.textViewHeader)
    }

    // View holder for transaction row item
    internal inner class TransactionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTextViewTransactionDate: TextView = v.findViewById(R.id.textViewTransactionDate)
        var mTextViewTransactionDescription: TextView = v.findViewById(R.id.textViewTransactionName)
        var mTextViewTransactionBalance: TextView = v.findViewById(R.id.textViewTransactionBalance)
    }
}

