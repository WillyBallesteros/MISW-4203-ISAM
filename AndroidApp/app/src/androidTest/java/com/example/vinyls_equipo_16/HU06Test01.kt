package com.example.vinyls_equipo_16

import android.view.Gravity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vinyls_equipo_16.ui.MainActivity
import com.example.vinyls_equipo_16.ui.adapters.CollectorsAdapter
import com.example.vinyls_equipo_16.viewmodels.CollectorViewModel
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HU06Test01 {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var idlingResource: GenericIdlingResource

    @Before
    fun setUp() {
        activityScenarioRule.scenario.onActivity { activity ->
            val viewModel = ViewModelProvider(activity).get(CollectorViewModel::class.java)
            idlingResource = GenericIdlingResource(viewModel.dataLoaded)
            IdlingRegistry.getInstance().register(idlingResource)
        }
    }

    @Test
    fun scrollToItem_clicksOnFirstItem() {

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()) // Open Drawer

        // Click on the first menu item
        onView(withId(R.id.collectorFragment)).perform(click())


        onView(withId(R.id.collectorsRv)).perform(
            RecyclerViewActions.scrollToPosition<CollectorsAdapter.CollectorViewHolder>(0)
        )



        onView(withId(R.id.collectorsRv))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CollectorsAdapter.CollectorViewHolder>(0, click()))

        activityScenarioRule.scenario.onActivity { activity ->
            val navController = activity.findNavController(R.id.nav_host_fragment_content_main)
            assertEquals(navController.currentDestination?.id, R.id.collectorDetailFragment)
        }

        onView(withId(R.id.name)).check(matches(isDisplayed()))
        onView(withId(R.id.telephone)).check(matches(isDisplayed()))
        onView(withId(R.id.email)).check(matches(isDisplayed()))

    }


    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        idlingResource.cleanup()
    }
}
