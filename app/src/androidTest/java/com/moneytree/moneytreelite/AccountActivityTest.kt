package com.moneytree.moneytreelite

import android.widget.TextView
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.moneytree.moneytreelite.views.AccountsActivity
import kotlinx.android.synthetic.main.activity_account.*
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AccountActivityTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule<AccountsActivity>(AccountsActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun ensureTextViewBalance() {
        val activity = rule.activity

        val viewById = activity.textViewTotalBalance

        assertThat(viewById, notNullValue())
        assertThat(viewById, instanceOf(TextView::class.java))
        val textView = viewById as TextView
        assertThat(textView.text.toString(), `is`("3057.5"))
    }
}