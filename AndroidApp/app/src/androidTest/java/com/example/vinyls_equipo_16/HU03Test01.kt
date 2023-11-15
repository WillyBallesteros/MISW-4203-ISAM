import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.ui.MainActivity
import com.example.vinyls_equipo_16.ui.adapters.AlbumsAdapter
import com.example.vinyls_equipo_16.ui.adapters.MusiciansAdapter
import com.example.vinyls_equipo_16.viewmodels.AlbumViewModel
import com.example.vinyls_equipo_16.viewmodels.MusicianViewModel
import junit.framework.TestCase.assertEquals
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HU03Test01 {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var idlingResource: GenericIdlingResource

    @Before
    fun setUp() {
        activityScenarioRule.scenario.onActivity { activity ->
            val viewModel = ViewModelProvider(activity).get(MusicianViewModel::class.java)
            idlingResource = GenericIdlingResource(viewModel.dataLoaded)
            IdlingRegistry.getInstance().register(idlingResource)
        }
    }

    @Test
    fun scrollToItem_clicksOnFirstItem() {
        onView(withId(R.id.musiciansRv)).perform(
            RecyclerViewActions.scrollToPosition<MusiciansAdapter.MusicianViewHolder>(0)
        )
        onView(allOf(withId(R.id.musician_image), isDescendantOfA(nthChildOf(withId(R.id.musiciansRv), 0))))
            .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.musician_name), isDescendantOfA(nthChildOf(withId(R.id.musiciansRv), 0))))
            .check(matches(isDisplayed()))
        /*
        onView(withId(R.id.musiciansRv))
            .perform(RecyclerViewActions.actionOnItemAtPosition<MusiciansAdapter.MusicianViewHolder>(0, click()))

        activityScenarioRule.scenario.onActivity { activity ->
            val navController = activity.findNavController(R.id.nav_host_fragment_content_main)
            assertEquals(navController.currentDestination?.id, R.id.musicianDetailFragment)
        }

        pressBack()

        activityScenarioRule.scenario.onActivity { activity ->
            val navController = activity.findNavController(R.id.nav_host_fragment_content_main)
            assertEquals(navController.currentDestination?.id, R.id.albumFragment)
        }
        */
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
