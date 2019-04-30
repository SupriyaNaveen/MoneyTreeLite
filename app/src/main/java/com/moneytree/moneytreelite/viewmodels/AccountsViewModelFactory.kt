package com.moneytree.moneytreelite.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class AccountsViewModelFactory @Inject constructor(
    private val accountsViewModel: AccountsViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountsViewModel::class.java)) {
            return accountsViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
