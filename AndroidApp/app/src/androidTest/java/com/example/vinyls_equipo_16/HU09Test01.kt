package com.example.vinyls_equipo_16

import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vinyls_equipo_16.ui.MainActivity
import com.example.vinyls_equipo_16.ui.adapters.AlbumsAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumViewModel
import junit.framework.TestCase
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Random

@RunWith(AndroidJUnit4::class)
class HU09Test01 {
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var idlingResource: GenericIdlingResource

    @Before
    fun setUp() {
        activityScenarioRule.scenario.onActivity { activity ->
            val viewModel = ViewModelProvider(activity).get(AlbumViewModel::class.java)
            idlingResource = GenericIdlingResource(viewModel.dataLoaded)
            IdlingRegistry.getInstance().register(idlingResource)
        }
    }

    @Test
    fun addCommentToAlbum_And_VerifyInList() {
        val randomId = Random().nextInt(1000)
        val description = "De los mejores albumes que he escuchado $randomId"
        val rating = 4


        Espresso.onView(ViewMatchers.withId(R.id.albumsRv)).perform(
            RecyclerViewActions.scrollToPosition<AlbumsAdapter.AlbumViewHolder>(0)
        )
        Espresso.onView(
            CoreMatchers.allOf(
                ViewMatchers.withId(R.id.album_cover),
                ViewMatchers.isDescendantOfA(nthChildOf(ViewMatchers.withId(R.id.albumsRv), 0))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(
            CoreMatchers.allOf(
                ViewMatchers.withId(R.id.album_name),
                ViewMatchers.isDescendantOfA(nthChildOf(ViewMatchers.withId(R.id.albumsRv), 0))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.albumsRv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AlbumsAdapter.AlbumViewHolder>(0,
                    ViewActions.click()
                ))

        Thread.sleep(2000)

        activityScenarioRule.scenario.onActivity { activity ->
            val navController = activity.findNavController(R.id.nav_host_fragment_content_main)
            TestCase.assertEquals(navController.currentDestination?.id, R.id.albumDetailFragment)
        }

        Espresso.onView(ViewMatchers.withId(R.id.comment_icon)).perform(ViewActions.click())

        Thread.sleep(2000)
        activityScenarioRule.scenario.onActivity { activity ->
            val navController = activity.findNavController(R.id.nav_host_fragment_content_main)
            TestCase.assertEquals(navController.currentDestination?.id, R.id.albumAddCommentFragment)
        }


        Espresso.onView(ViewMatchers.withId(R.id.textComment)).perform(ViewActions.typeText(description), ViewActions.pressImeActionButton())
        Espresso.onView(ViewMatchers.withId(R.id.ratingBar))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.btnCreate)).perform(ViewActions.click())

        Thread.sleep(2000)

        activityScenarioRule.scenario.onActivity { activity ->
            val navController = activity.findNavController(R.id.nav_host_fragment_content_main)
            TestCase.assertEquals(navController.currentDestination?.id, R.id.albumDetailFragment)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        idlingResource.cleanup()
    }

    private fun nthChildOf(parentMatcher: Matcher<View>, childPosition: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("with $childPosition child view of type parentMatcher")
            }

            override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) {
                    return parentMatcher.matches(view.parent)
                }

                val group = view.parent as ViewGroup
                return parentMatcher.matches(view.parent) && group.getChildAt(childPosition) == view
            }
        }
    }
}