package com.moneytree.moneytreelite.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class TransactionViewModelFactory @Inject constructor(
    private val transactionViewModel: TransactionViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return transactionViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
