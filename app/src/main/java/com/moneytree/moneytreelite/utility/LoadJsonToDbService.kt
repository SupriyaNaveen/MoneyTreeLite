package com.moneytree.moneytreelite.utility

import android.app.IntentService
import android.content.Intent
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.moneytree.moneytreelite.R
import com.moneytree.moneytreelite.repository.data.Account
import com.moneytree.moneytreelite.repository.data.Transaction
import com.moneytree.moneytreelite.repository.db.AppDatabase
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Service called only when App class onCreate() called.
 * Load JSON file from assets and read them.
 * Convert to ArrayList<Account> using gson library then insert to db.
 * Separate worker thread. StopSelf when job done.
 */
class LoadJsonToDbService : IntentService(LoadJsonToDbService::class.qualifiedName) {
    override fun onHandleIntent(intent: Intent?) {
        val bufferedReader: BufferedReader =
            InputStreamReader(assets.open(getString(R.string.file_name_account))).buffered()
        val inputString = bufferedReader.use { it.readText() }

        val listType = object : TypeToken<List<Account>>() {

        }.type

        val jsonObject = JSONObject(inputString)
        val jsonArray = jsonObject.getJSONArray(getString(R.string.header_tag_accounts))

        val gsonObj = GsonBuilder().create()
        val accountList: ArrayList<Account> = gsonObj.fromJson(jsonArray.toString(), listType)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
        db.accountDao().insertAll(accountList)


        //Load transaction data
        for (i in 1..3) {
            val bufferedTransactionReader: BufferedReader =
                InputStreamReader(assets.open(getString(R.string.file_name_transaction) + i + ".json")).buffered()
            val inputTransString = bufferedTransactionReader.use { it.readText() }

            val listTransType = object : TypeToken<List<Transaction>>() {

            }.type

            val builder = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create()
            val jsonTransactionObject = JSONObject(inputTransString)
            val jsonTransactionArray = jsonTransactionObject.getJSONArray(getString(R.string.header_tag_transactions))

            val transactionList: ArrayList<Transaction> =
                builder.fromJson(jsonTransactionArray.toString(), listTransType)
            db.transactionDao().insertAll(transactionList)
        }

    }
}