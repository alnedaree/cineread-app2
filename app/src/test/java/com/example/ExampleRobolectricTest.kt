package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Cinera", appName)
    }

    @Test
    fun `launch main activity and navigate tabs cleanly`() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("hero_novel_card").assertExists()

        composeTestRule.onNodeWithTag("nav_gallery").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("gallery_tab_characters").assertExists()

        composeTestRule.onNodeWithTag("nav_voices").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("nav_settings").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("settings_lang_en").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("nav_library").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("hero_play_cinema_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("cinema_play_pause_button").assertExists()
    }
}
