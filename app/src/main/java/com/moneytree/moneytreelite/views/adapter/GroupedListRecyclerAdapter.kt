package com.moneytree.moneytreelite.views.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.moneytreelite.R
import com.moneytree.moneytreelite.utility.Constants
import com.moneytree.moneytreelite.views.TransactionActivity
import java.util.*

class GroupedListRecyclerAdapter(context: Context, consolidatedList: List<ListItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var consolidatedList: List<ListItem> = ArrayList()
    private var context: Context

    init {
        this.consolidatedList = consolidatedList
        this.context = context
    }

    override fun getItemViewType(position: Int): Int {
        return consolidatedList[position].getType()
    }

    override fun getItemCount(): Int {
        return consolidatedList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {

            ListItem.TYPE_ACCOUNT -> {
                val v1 = inflater.inflate(
                    R.layout.layout_account_row, parent,
                    false
                )
                viewHolder = AccountViewHolder(v1)
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

            ListItem.TYPE_ACCOUNT -> {
                val accountItem = consolidatedList[position] as AccountItem
                val accountViewHolder = viewHolder as AccountViewHolder
                accountItem.account?.run {
                    accountViewHolder.mTextViewAccountName.text = accountName
                    accountViewHolder.mTextAccountBalance.text = currency.plus(currentBalance)
                }
                //Row clicked
                accountViewHolder.itemView.setOnClickListener {

                    accountItem.account?.also { account ->
                        val intent = Intent(context, TransactionActivity::class.java).apply {
                            putExtra(Constants.ACCOUNT_ID_INTENT_KEY, account.id)
                            putExtra(Constants.INSTITUTION_INTENT_KET, account.institution)
                            putExtra(Constants.ACCOUNT_NAME_INTENT_KEY, account.accountName)
                            putExtra(Constants.ACCOUNT_BALANCE_INTENT_KEY, account.currentBalance)
                        }
                        context.startActivity(intent)
                    }
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

    fun updateAccountsData(consolidatedList: ArrayList<ListItem>) {
        this.consolidatedList = consolidatedList
        notifyDataSetChanged()
    }


    // ViewHolder for header row item
    internal inner class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mHeaderText: TextView = v.findViewById(R.id.textViewHeader)
    }

    // View holder for account row item
    internal inner class AccountViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTextViewAccountName: TextView = v.findViewById(R.id.textViewAccountName)
        var mTextAccountBalance: TextView = v.findViewById(R.id.textViewAccountBalance)
    }
}

