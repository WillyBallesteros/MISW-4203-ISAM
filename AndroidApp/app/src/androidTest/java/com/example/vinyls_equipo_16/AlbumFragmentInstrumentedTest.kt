package com.example.vinyls_equipo_16


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText


@RunWith(AndroidJUnit4::class)
class AlbumFragmentInstrumentedTest {

    @Test
    fun testAlbumAddedToList() {
        Thread.sleep(4000)
        // Assuming that an album with title "Test Album" has been added to the list
        onView(withId(R.id.textView6))
            .check(matches(isDisplayed()))  // Check if the RecyclerView is displayed

        onView(withText("Buscando América"))  // Replace "Test Album" with the actual title of the album
            .check(matches(isDisplayed()))  // Check if the album title is displayed
    }
}