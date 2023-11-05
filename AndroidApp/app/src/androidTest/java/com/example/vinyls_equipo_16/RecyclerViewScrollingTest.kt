import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.vinyls_equipo_16.ui.AlbumFragment
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.ui.MainActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.vinyls_equipo_16.ui.adapters.AlbumsAdapter
import androidx.test.espresso.action.ViewActions.click

@RunWith(AndroidJUnit4::class)
class RecyclerViewScrollingTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun scrollToItem_clicksOnFirstItem() {
        Thread.sleep(5000)

        onView(withId(R.id.albumsRv))
            .perform(RecyclerViewActions.scrollToPosition<AlbumsAdapter.AlbumViewHolder>(0))
            .perform(RecyclerViewActions.actionOnItemAtPosition<AlbumsAdapter.AlbumViewHolder>(0, click()))
    }
}