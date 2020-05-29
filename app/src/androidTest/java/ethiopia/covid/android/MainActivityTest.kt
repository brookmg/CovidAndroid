package ethiopia.covid.android

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ethiopia.covid.android.ui.activity.MainActivity
import ethiopia.covid.android.util.Utils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var mainActivity: MainActivity

    @Rule
    @JvmField
    val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        mainActivity = mActivityRule.activity
    }

    @Test
    fun appLaunchSuccessful() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun showLanguageDialog() {
        onView(withId(R.id.lang_btn)).perform(click())
        onView(withText(mainActivity.getString(R.string.language)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
    }

    @Test
    fun changeTheme() {
        val previousThemeValue = Utils.getCurrentTheme(mainActivity)
        onView(withId(R.id.theme_btn)).perform(click())
        val newThemeValue = Utils.getCurrentTheme(mainActivity)
        assert(previousThemeValue != newThemeValue)
    }
}