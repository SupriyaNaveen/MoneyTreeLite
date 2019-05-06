package com.moneytree.moneytreelite.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.moneytreelite.R
import com.moneytree.moneytreelite.viewmodels.AccountsViewModel
import com.moneytree.moneytreelite.viewmodels.AccountsViewModelFactory
import com.moneytree.moneytreelite.views.adapter.GroupedListRecyclerAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Activity presents
 * In the account list, a user should be able to see a list of accounts and the total balance
 * of those accounts in the user's currency. The list should be ordered by the account
 * nickname and grouped by institution. Each row should display the nickname and the
 * balance in that account's currency.
 */
class AccountsActivity : AppCompatActivity(), CoroutineScope {

    // Recycler adapter grouped by institution and account data in account name order.
    private lateinit var mAccountsAdapter: GroupedListRecyclerAdapter

    //Init view model
    private lateinit var mAccountsViewModel: AccountsViewModel

    //Inject view model factory
    @Inject
    lateinit var mAccountsViewModelFactory: AccountsViewModelFactory

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    /**
     * Android Injection injected.
     * Get the View Model from ViewModelProvider class.
     * Set the adapter with ListItem : Header, Account list belong to header.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        mAccountsViewModel = ViewModelProviders.of(this, this.mAccountsViewModelFactory).get(
            AccountsViewModel::class.java
        )
        mAccountsAdapter =
            GroupedListRecyclerAdapter(this, ArrayList())
        initializeRecycler()
        loadAccountData()

        launch {
            mAccountsViewModel.getSumOfAccountsBalance().observe(this@AccountsActivity, Observer {
                textViewTotalBalance.text = it.toString()
            })
        }

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.account_title)
        }
    }

    /**
     * Initialize the recycler adapter with Layout Manager.
     */
    private fun initializeRecycler() {
        GridLayoutManager(this, 1).apply {
            orientation = RecyclerView.VERTICAL
        }.also { gridLayoutManager ->
            recyclerViewAccounts.apply {
                setHasFixedSize(true)
                layoutManager = gridLayoutManager
            }
        }

        recyclerViewAccounts.adapter = mAccountsAdapter
    }

    /**
     * LiveData of account table: ORDER BY account name.
     * The account data is grouped to tree hash map with its institution name.
     * Each group name includes, list of account with that institution.
     * Then consolidate the tree hash map to header and account type to show in adapter.
     *
     * The Scope functions used for functionality.
     */
    private fun loadAccountData() {
        launch {
            mAccountsViewModel.getAccounts().observe(this@AccountsActivity, Observer {

                mAccountsViewModel.groupAccountsByInstitution(ArrayList(it)).let { groupedTreeMap ->
                    mAccountsViewModel.prepareListForRecyclerView(groupedTreeMap).let { consolidatedList ->
                        mAccountsAdapter.updateAccountsData(consolidatedList)
                    }
                }
            })
        }
    }

}
