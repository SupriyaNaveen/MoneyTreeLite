package com.moneytree.moneytreelite.views

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.moneytreelite.R
import com.moneytree.moneytreelite.utility.Constants
import com.moneytree.moneytreelite.viewmodels.TransactionViewModel
import com.moneytree.moneytreelite.viewmodels.TransactionViewModelFactory
import com.moneytree.moneytreelite.views.adapter.ListItem
import com.moneytree.moneytreelite.views.adapter.TransactionItem
import com.moneytree.moneytreelite.views.adapter.TransactionListRecyclerAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_transactions.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TransactionActivity : AppCompatActivity()
    , CoroutineScope {

    private lateinit var mTransactionsAdapter: TransactionListRecyclerAdapter

    //Init view model
    private lateinit var mTransactionViewModel: TransactionViewModel

    //Inject view model factory
    @Inject
    lateinit var mTransactionViewModelFactory: TransactionViewModelFactory

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    /**
     * Android Injection for dagger used.
     * Get the account details from intent bundle and load transaction data.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = intent.getStringExtra(Constants.INSTITUTION_INTENT_KET)
        }

        mTransactionViewModel = ViewModelProviders.of(this, mTransactionViewModelFactory).get(
            TransactionViewModel::class.java
        )

        mTransactionsAdapter = TransactionListRecyclerAdapter(ArrayList())
        initializeRecycler()
        // Get the account id from selected account. Load the transaction details.
        // I can pass Account bundle itself parcelization
        // I can pass account id then get Account by querying DB.
        textViewAccountName.text = intent.getStringExtra(Constants.ACCOUNT_NAME_INTENT_KEY)
        textViewAccountBalance.text = intent.getFloatExtra(Constants.ACCOUNT_BALANCE_INTENT_KEY, 0.0f).toString()

        intent.getIntExtra(Constants.ACCOUNT_ID_INTENT_KEY, 0).also { accountId ->
            loadData(accountId)
            loadTitle(accountId)
        }
    }

    private fun loadTitle(accountId: Int) {
        launch {
            val titleString = mTransactionViewModel.getTransactionSum(accountId)
            textViewTotalBalance.text = titleString
        }
    }

    /**
     * Initialize the recycler adapter with Layout Manager.
     */
    private fun initializeRecycler() {
        GridLayoutManager(this, 1).apply {
            orientation = RecyclerView.VERTICAL
        }.also { gridLayoutManager ->
            recyclerViewTransactions.apply {
                setHasFixedSize(true)
                layoutManager = gridLayoutManager
            }
        }

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (ListItem.TYPE_TRANSACTION == mTransactionsAdapter.getTransactionData()[position].getType()) {
                    showDeleteConfirmationDialog(mTransactionsAdapter.getTransactionData()[position] as TransactionItem)
                } else {
                    mTransactionsAdapter.notifyDataSetChanged()
                }
            }
        }
        ItemTouchHelper(swipeHandler).apply {
            attachToRecyclerView(recyclerViewTransactions)
        }

        recyclerViewTransactions.adapter = mTransactionsAdapter
    }

    /**
     * Get the transaction list based on account id.
     * Transaction data in date order (database query)
     * Group by month. Linked hash map used to maintain insertion order.
     * Prepare the data for recycler adapter.
     * ListItem includes header as month year data and transaction list belong to each month.
     */
    private fun loadData(accountId: Int) {
        launch {
            mTransactionViewModel.getTransactions(accountId).observe(this@TransactionActivity, Observer {
                mTransactionViewModel.groupTransactionByMonth(ArrayList(it)).let { groupedHashMap ->
                    mTransactionViewModel.prepareListForRecyclerView(groupedHashMap).let { consolidatedList ->
                        mTransactionsAdapter.updateTransactionData(consolidatedList)
                    }
                }
            })
        }
    }

    /**
     * Before deleting transaction confirm from user.
     */
    private fun showDeleteConfirmationDialog(transactionItem: TransactionItem) {
        AlertDialog.Builder(this).apply {
            // Set the alert dialog title
            setTitle(getString(R.string.delete_transaction))

            // Display a message on alert dialog
            setMessage(getString(R.string.message_confirm_delete))

            // Set a positive button and its click listener on alert dialog
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                launch {
                    mTransactionViewModel.deleteTransaction(transactionItem.transaction)
                }
            }
            setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
                mTransactionsAdapter.notifyDataSetChanged()
                dialogInterface.cancel()
            }

        }.create().show()
    }
}
