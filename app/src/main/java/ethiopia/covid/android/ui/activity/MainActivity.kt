package ethiopia.covid.android.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.franmontiel.localechanger.LocaleChanger
import com.franmontiel.localechanger.utils.ActivityRecreationHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ethiopia.covid.android.R
import ethiopia.covid.android.data.QuestionnaireItem
import ethiopia.covid.android.ui.fragment.BaseFragment
import ethiopia.covid.android.ui.fragment.HeatMapFragment
import ethiopia.covid.android.ui.fragment.HomeFragment
import ethiopia.covid.android.ui.fragment.QuestionnaireFragment
import ethiopia.covid.android.util.Constant.PREFERENCE_QTIME
import ethiopia.covid.android.util.Constant.TAG_HEAT_MAP
import ethiopia.covid.android.util.Constant.TAG_HOME
import ethiopia.covid.android.util.Constant.TAG_QUESTIONNAIRE
import ethiopia.covid.android.util.Constant.getQuestionnaireConstant
import ethiopia.covid.android.util.Utils.getCurrentTheme
import ethiopia.covid.android.util.Utils.md5
import ethiopia.covid.android.util.Utils.setCurrentTheme
import me.ibrahimsn.lib.Badge
import mumayank.com.airlocationlibrary.AirLocation
import mumayank.com.airlocationlibrary.AirLocation.LocationFailedEnum
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

@Suppress("unused")
class MainActivity : AppCompatActivity() {
    private lateinit var _fragmentContainer: FrameLayout
    private lateinit var currentFragment: WeakReference<Fragment?>
    private lateinit var airLocation: AirLocation
    private val mainCallbacks: MutableList<AirLocation.Callbacks> = ArrayList()

    private fun setCurrentFragment(fragment: BaseFragment) {
        currentFragment = WeakReference(fragment)
    }

    fun registerLocationCallback(callbacks: AirLocation.Callbacks) {
        if (!mainCallbacks.contains(callbacks)) mainCallbacks.add(callbacks)
    }

    fun unRegisterLocationCallback(callbacks: AirLocation.Callbacks) {
        mainCallbacks.remove(callbacks)
    }

    fun setBadge(position: Int, badge: Badge?) {
        (currentFragment.get() as? HomeFragment)?.showBadge(position, badge)
    }

    fun removeBadge(position: Int) {
        (currentFragment.get() as? HomeFragment)?.removeBadge(position)
    }

    fun showLanguageDialog() {
        MaterialAlertDialogBuilder(this, if (getCurrentTheme(this) == 0) R.style.LightAlertDialog else R.style.DarkAlertDialog)
                .setTitle(getString(R.string.language))
                .setItems(arrayOf(getString(R.string.amh), getString(R.string.eng), getString(R.string.tig), getString(R.string.or))) {
                    _: DialogInterface?, which: Int ->
                    when (which) {
                        0 -> LocaleChanger.setLocale(Locale("am", "et"))
                        1 -> LocaleChanger.setLocale(Locale("en", "us"))
                        2 -> LocaleChanger.setLocale(Locale("ti", "et"))
                        3 -> LocaleChanger.setLocale(Locale("om", "et"))
                        else -> LocaleChanger.setLocale(Locale("en", "us"))
                    }
                    recreateActivity()
                }.show()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleChanger.configureBaseContext(newBase))
    }

    private fun recreateActivity() {
        ActivityRecreationHelper.recreate(this, false)
    }

    private fun fragStarter(fragTag: String, baseFragment: BaseFragment, @Suppress("UNUSED_PARAMETER") bundle: Bundle, sharedView: View?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, baseFragment, fragTag)
        if (sharedView != null) {
            val transitionName = ViewCompat.getTransitionName(sharedView)
            fragmentTransaction.addSharedElement(sharedView, transitionName ?: "nothing_here")
        }
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        setCurrentFragment(baseFragment)
    }

    fun changeFragment(fragmentTag: String, bundle: Bundle, view: View?) {
        // TODO: 5/19/2020 Decouple logic from the view.
        when (fragmentTag) {
            TAG_HOME -> {
                val baseFragment: BaseFragment = HomeFragment.newInstance()
                fragStarter(fragmentTag, baseFragment, bundle, view)
            }
            TAG_QUESTIONNAIRE -> {
                val lastQuiz = getDefaultSharedPreferences(this).getLong(PREFERENCE_QTIME, 0)

                if (Date().time - lastQuiz > DateUtils.DAY_IN_MILLIS) {
                    val questionnaireContent = FirebaseRemoteConfig.getInstance()
                            .getString(getQuestionnaireConstant(LocaleChanger.getLocale().language))
                    val hashOfQuestionnaire = md5(questionnaireContent)
                    val items = Gson()
                            .fromJson<List<QuestionnaireItem>>(questionnaireContent, object : TypeToken<List<QuestionnaireItem?>?>() {}
                                    .type)
                    if (items != null && items.isNotEmpty()) {
                        val baseFragment: BaseFragment = QuestionnaireFragment.newInstance(items, hashOfQuestionnaire)
                        fragStarter(fragmentTag, baseFragment, bundle, view)
                    }
                } else {
                    Snackbar.make(_fragmentContainer, getString(R.string.comeback_tmw), Snackbar.LENGTH_SHORT).show()
                }
            }

            TAG_HEAT_MAP -> {
                val baseFragment: BaseFragment = HeatMapFragment.newInstance()
                fragStarter(fragmentTag, baseFragment, bundle, view)
            }
        }
    }

    override fun onBackPressed() {
        if ((currentFragment.get() as? BaseFragment)?.canGoBack() == true) {
            callBackOnParentFragment()
        } else {
            if (supportFragmentManager.backStackEntryCount > 1) {
                //there are more items in the back stack. We are not on the home frag
                supportFragmentManager.popBackStackImmediate()
            } else {
                Snackbar.make(_fragmentContainer, getString(R.string.exit_text), Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.exit)) { finish() }.show()
            }
            if (supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] is BaseFragment) setCurrentFragment(supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] as BaseFragment)
        }
    }

    fun forcefulOnBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            //there are more items in the back stack. We are not on the home frag
            supportFragmentManager.popBackStackImmediate()
        } else {
            Snackbar.make(_fragmentContainer, "Press back again or 👉🏾 button", Snackbar.LENGTH_SHORT)
                    .setAction("Exit") { finish() }.show()
        }
        if (supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] is BaseFragment)
            setCurrentFragment(supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] as BaseFragment)
    }

    fun changeTheme() {
        val currentTheme = getCurrentTheme(this)
        if (currentTheme == 0) setCurrentTheme(this, 1) else setCurrentTheme(this, 0)
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

    fun callNextOnParentFragment() = (currentFragment.get() as? BaseFragment)?.next()

    fun callBackOnParentFragment() = (currentFragment.get() as? BaseFragment)?.back()

    fun requestLocationForAll() {
        airLocation = AirLocation(
                this, true,
                shouldWeRequestOptimization = true, callbacks = object : AirLocation.Callbacks {
            override fun onSuccess(location: Location) {
                for (callback in mainCallbacks) callback.onSuccess(location)
            }

            override fun onFailed(locationFailedEnum: LocationFailedEnum) {
                for (callback in mainCallbacks) callback.onFailed(locationFailedEnum)
            }
        })
    }

    fun requestLocationIndividually(callbacks: AirLocation.Callbacks) {
        airLocation = AirLocation(
                this, true,
                shouldWeRequestOptimization = true, callbacks = object : AirLocation.Callbacks {
            override fun onSuccess(location: Location) {
                callbacks.onSuccess(location)
            }

            override fun onFailed(locationFailedEnum: LocationFailedEnum) {
                callbacks.onFailed(locationFailedEnum)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(if (getCurrentTheme(this) == 0) R.style.LightTheme else R.style.DarkTheme)
        setContentView(R.layout.activity_main)
        _fragmentContainer = findViewById(R.id.fragmentContainer)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) if (getCurrentTheme(this) == 0) window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        changeFragment(TAG_HOME, Bundle(), null)
        FirebaseRemoteConfig.getInstance().fetchAndActivate()
                .addOnSuccessListener { aBoolean: Boolean ->
                    Timber.d("Firebase Remote Config %s",
                            if (aBoolean) "Updated" else "!Updated")
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}