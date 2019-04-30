package com.moneytree.moneytreelite.repository.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


/**
 * Account data class
 */
@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int,

    @SerializedName("name")
    @ColumnInfo(name = "account_name")
    var accountName: String? = null,

    @SerializedName("institution")
    @ColumnInfo(name = "institution")
    val institution: String? = null,

    @SerializedName("currency")
    @ColumnInfo(name = "currency")
    var currency: String? = null,

    @SerializedName("current_balance")
    @ColumnInfo(name = "current_balance")
    var currentBalance: Float,

    @SerializedName("current_balance_in_base")
    @ColumnInfo(name = "current_balance_in_base")
    var currentBalanceInBase: Float
) : Serializable

/**
 * Transaction data class
 */
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int,

    @SerializedName("account_id")
    @ColumnInfo(name = "account_id")
    var accountId: Int,

    @SerializedName("category_id")
    @ColumnInfo(name = "category_id")
    val categoryId: String? = null,

    @SerializedName("date")
    @ColumnInfo(name = "date")
    var date: Date? = null,

    @SerializedName("amount")
    @ColumnInfo(name = "amount")
    var amount: Float,

    @SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String? = null
) : Serializable

object DateTypeConverters {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Date? {
        return Date(value!!)
    }

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time!!.toLong()
    }
}



