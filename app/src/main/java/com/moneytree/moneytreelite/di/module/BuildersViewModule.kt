package com.moneytree.moneytreelite.di.module

import com.moneytree.moneytreelite.views.AccountsActivity
import com.moneytree.moneytreelite.views.TransactionActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * View related module instance.
 */
@Module
abstract class BuildersViewModule {

    @ContributesAndroidInjector
    abstract fun contributeAccountsActivity(): AccountsActivity

    @ContributesAndroidInjector
    abstract fun contributeTransactionActivity(): TransactionActivity
}