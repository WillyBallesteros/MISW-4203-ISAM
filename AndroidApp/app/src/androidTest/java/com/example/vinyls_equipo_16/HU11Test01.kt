import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.vinyls_equipo_16.GenericIdlingResource
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.ui.MainActivity
import com.example.vinyls_equipo_16.ui.adapters.AlbumsAdapter
import com.example.vinyls_equipo_16.ui.adapters.CollectorsAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumViewModel
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Random

@RunWith(AndroidJUnit4::class)
class HU11Test01 {
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
    fun add_Album_To_Collector() {
        val priceAlbum = Random().nextInt(1000)

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()) // Open Drawer

        // Click on the first menu item
        onView(withId(R.id.collectorFragment)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.collectorsRv)).perform(
            RecyclerViewActions.scrollToPosition<CollectorsAdapter.CollectorViewHolder>(0)
        )
        onView(allOf(withId(R.id.collector_name), isDescendantOfA(nthChildOf(withId(R.id.collectorsRv), 0))))
            .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.collector_telephone), isDescendantOfA(nthChildOf(withId(R.id.collectorsRv), 0))))
            .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.collector_email), isDescendantOfA(nthChildOf(withId(R.id.collectorsRv), 0))))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collectorsRv)).perform(
            RecyclerViewActions.scrollToPosition<CollectorsAdapter.CollectorViewHolder>(0)
        )

        onView(withId(R.id.collectorsRv))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CollectorsAdapter.CollectorViewHolder>(0, click()))

        onView(withId(R.id.add_button_collector)).perform(click())
        activityScenarioRule.scenario.onActivity { activity ->
            val navController = activity.findNavController(R.id.nav_host_fragment_content_main)
            assertEquals(navController.currentDestination?.id, R.id.collectorAddAlbumFragment)
        }

        onView(withId(R.id.priceAlbum)).perform(ViewActions.typeText(priceAlbum.toString()))
        onView(ViewMatchers.withId(R.id.btnCreate)).perform(ViewActions.click())

        Thread.sleep(1000)

        activityScenarioRule.scenario.onActivity { activity ->
            val navController = activity.findNavController(R.id.nav_host_fragment_content_main)
            assertEquals(navController.currentDestination?.id, R.id.collectorDetailFragment)
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
