package com.moneytree.moneytreelite.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.moneytreelite.R
import com.moneytree.moneytreelite.utility.Constants
import com.moneytree.moneytreelite.viewmodels.TransactionViewModel
import com.moneytree.moneytreelite.viewmodels.TransactionViewModelFactory
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

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        mTransactionViewModel = ViewModelProviders.of(this, this.mTransactionViewModelFactory).get(
            TransactionViewModel::class.java
        )

        mTransactionsAdapter = TransactionListRecyclerAdapter(ArrayList())
        initializeRecycler()
        // Get the account id from selected account. Load the transaction details.
        // I can pass Account bundle itself parcelization
        // I can pass account id then get Account by querying DB.
        val accountId = intent.getIntExtra(Constants.ACCOUNT_ID_INTENT_KEY, 0)
        supportActionBar!!.title = intent.getStringExtra(Constants.INSTITUTION_INTENT_KET)
        textViewAccountName.text = intent.getStringExtra(Constants.ACCOUNT_NAME_INTENT_KEY)
        textViewAccountBalance.text = intent.getFloatExtra(Constants.ACCOUNT_BALANCE_INTENT_KEY, 0.0f).toString()
        loadData(accountId)
        loadTitle(accountId)
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
        val gridLayoutManager = GridLayoutManager(this, 1)
        gridLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewTransactions.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
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
            val transactionList = mTransactionViewModel.getTransactions(accountId)
            val groupedHashMap = mTransactionViewModel.groupTransactionByMonth(ArrayList(transactionList))
            val consolidatedList = mTransactionViewModel.prepareListForRecyclerView(groupedHashMap)
            mTransactionsAdapter.updateTransactionData(consolidatedList)
        }
    }
}
