package com.moneytree.moneytreelite.utility

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Provides generic static method.
 */
fun getDateInFormat(outputFormatString: String, dateString: Date): String {
    val outputFormatter = SimpleDateFormat(outputFormatString, Locale.ENGLISH)
    var resultString = " "
    try {
        resultString = outputFormatter.format(dateString)
    } catch (ex: ParseException) {
        Timber.e(ex.localizedMessage)
    }
    return resultString
}