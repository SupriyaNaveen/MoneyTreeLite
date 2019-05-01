package com.moneytree.moneytreelite

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.moneytree.moneytreelite.views.TransactionActivity
import kotlinx.android.synthetic.main.activity_account.*
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TransactionActivityTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule<TransactionActivity>(TransactionActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun ensureTextViewBalance() {
        val activity = rule.activity

        val viewById = activity.textViewTotalBalance

        assertThat(viewById, notNullValue())
        assertThat(viewById, instanceOf(TextView::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testSwipeRight() {
        onView(withId(R.id.recyclerViewTransactions)).perform(swipeRight());
    }

    private fun swipeRight(): ViewAction {
        return GeneralSwipeAction(
            Swipe.FAST, GeneralLocation.CENTER_LEFT,
            GeneralLocation.CENTER_RIGHT, Press.FINGER
        )
    }

}