package ethiopia.covid.android.ui.fragment

import android.animation.ValueAnimator
import android.content.DialogInterface
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ethiopia.covid.android.R
import ethiopia.covid.android.data.QuestionItem
import ethiopia.covid.android.data.QuestionnaireItem
import ethiopia.covid.android.ui.activity.MainActivity
import ethiopia.covid.android.ui.adapter.OnQuestionItemSelected
import ethiopia.covid.android.ui.adapter.TabAdapter
import ethiopia.covid.android.ui.fragment.IntroductionFragment.Companion.newInstance
import ethiopia.covid.android.ui.widget.YekomeViewPager
import ethiopia.covid.android.util.Utils.getCurrentTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mumayank.com.airlocationlibrary.AirLocation
import mumayank.com.airlocationlibrary.AirLocation.LocationFailedEnum
import timber.log.Timber
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class QuestionnaireFragment private constructor(
        questionnaireItems: MutableList<QuestionnaireItem>, hashOfQuestionnaire: String)
    : BaseFragment() {

    private var pageChangeProgressBar: ProgressBar? = null
    private var mainViewPager: YekomeViewPager? = null
    private var nextButton: FloatingActionButton? = null
    private var exitAllButton: AppCompatImageView? = null
    private var tabAdapter: TabAdapter? = null
    private val questionnaireItems: List<QuestionnaireItem>
    private val hashOfQuestionnaire: String
    private var bottomAppBar: BottomAppBar? = null
    private val introductionFragment = newInstance()
    private var resultFragment = ResultFragment.newInstance(HashMap(), "")
    private var currentLocation: Location? = null
    private val questionState = SparseArray<MutableList<QuestionItem>>()

    private val locationCallbacks: AirLocation.Callbacks = object : AirLocation.Callbacks {
        override fun onSuccess(location: Location) {
            currentLocation = location
        }

        override fun onFailed(locationFailedEnum: LocationFailedEnum) {
            Timber.e(locationFailedEnum.name)
        }
    }

    override fun back() {
        mainViewPager?.setCurrentItem(
        (mainViewPager?.currentItem?.minus(1))
                ?.coerceAtLeast(0) ?: 0, true
        )
        nextButton?.show()
        bottomAppBar?.performShow()
        canGoBack = mainViewPager?.currentItem != 0
    }

    override fun next() {
        if (mainViewPager?.currentItem?.inc() == tabAdapter?.count?.minus(1)) {
            val questionnaireItemListMap: MutableMap<QuestionnaireItem, MutableList<QuestionItem>> = HashMap()
            for (i in questionnaireItems.indices) questionnaireItemListMap[questionnaireItems[i]] = questionState[i] ?: mutableListOf()

            resultFragment.setMap(questionnaireItemListMap)
            resultFragment.setCurrentLocation(currentLocation)
            mainViewPager?.setCurrentItem(
                    (mainViewPager?.currentItem?.inc())?.coerceAtMost(tabAdapter?.count ?: 0) ?: 0
                    , true
            )

            nextButton?.hide()
            bottomAppBar?.performHide()
        }
        mainViewPager?.setCurrentItem(
                (mainViewPager?.currentItem?.inc())?.coerceAtMost(tabAdapter?.count ?: 0) ?: 0
                , true
        )
        canGoBack = mainViewPager?.currentItem != 0
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private fun handleWindowInsets(view: View?) {
        view?.setOnApplyWindowInsetsListener { _: View?, insets: WindowInsets ->
            val marginParams = view.layoutParams as MarginLayoutParams
            marginParams.setMargins(0, insets.systemWindowInsetTop, 0, 0)
            view.layoutParams = marginParams
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mainViewPager?.requestApplyInsets()
            handleWindowInsets(mainViewPager)
            handleWindowInsets(exitAllButton)
        }
    }

    private suspend fun runOnBackgroundThread() {
        withContext(Dispatchers.Default) {
            for (questionnaire in questionnaireItems) {
                val questionItemSelected = object : OnQuestionItemSelected {
                    override fun onItemSelected(item: QuestionItem, position: Int) {
                        val questionPositionInQuestionnaire = questionnaireItems.indexOf(questionnaire)
                        if (questionnaire.questionType === QuestionnaireItem.QuestionType.SINGLE_BLOCK_QUESTION) {
                            questionState.put(questionPositionInQuestionnaire, mutableListOf(item))
                            next()
                        } else if (questionnaire.questionType === QuestionnaireItem.QuestionType.SINGLE_CHOICE_QUESTION) {
                            if ( // If the user unselected an item ... ( clicked it twice in a row ) ... then put empty list
                                    questionState[questionPositionInQuestionnaire] != null &&
                                    questionState[questionPositionInQuestionnaire]!!.contains(item)) questionState.put(questionPositionInQuestionnaire, ArrayList()) else questionState.put(questionPositionInQuestionnaire, ArrayList(listOf(item)))
                        } else if (questionnaire.questionType === QuestionnaireItem.QuestionType.SINGLE_MULTIPLE_CHOICE_QUESTION) {
                            val items = questionState[questionPositionInQuestionnaire]
                            if (items != null) {
                                if (items.contains(item)) items.remove(item) else items.add(item)
                                questionState.put(questionPositionInQuestionnaire, items)
                            } else questionState.put(questionPositionInQuestionnaire, ArrayList(listOf(item)))
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    tabAdapter?.addFragment(QuestionPageFragment.newInstance(
                            questionnaire.questionText,
                            questionnaire.questionType,
                            questionnaire.questionItems,
                            questionItemSelected
                    ), "Question")
                    tabAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private suspend fun runOnUIThread() {
        withContext(Dispatchers.Main) {
            tabAdapter?.addFragment(resultFragment, "Result")
            tabAdapter?.notifyDataSetChanged()
            introductionFragment.changeProgressState(false)
            mainViewPager?.offscreenPageLimit = tabAdapter?.count ?: 0

            val valueAnimator = ValueAnimator.ofFloat(pageChangeProgressBar?.progress?.toFloat()
                    ?: 0f, 1f / (tabAdapter?.count?.toFloat()?.times(100f) ?: 1f))
            valueAnimator.addUpdateListener { animation: ValueAnimator ->
                pageChangeProgressBar?.progress = (animation.animatedValue as Float).roundToInt()
            }

            valueAnimator.start()
            nextButton?.setOnClickListener { next() }
            bottomAppBar?.setNavigationOnClickListener { (activity as? MainActivity)?.callBackOnParentFragment() }
            exitAllButton?.setOnClickListener {
                if (activity != null) MaterialAlertDialogBuilder(
                        activity,
                        if (getCurrentTheme(activity) == 0) R.style.LightAlertDialog else R.style.DarkAlertDialog
                ).setTitle(getString(R.string.exit_questionnaire))
                        .setMessage(getString(R.string.exit_questionnaire_message))
                        .setPositiveButton(getString(R.string.yes)) { _: DialogInterface?, _: Int ->
                            if (activity is MainActivity) {
                                (activity as MainActivity?)!!.forcefulOnBackPressed()
                            }
                        }.setNegativeButton(getString(R.string.no), null).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.questionnaire_fragment, container, false)
        (activity as? MainActivity)?.requestLocationIndividually(locationCallbacks)

        mainViewPager = mainView.findViewById(R.id.main_view_pager)
        pageChangeProgressBar = mainView.findViewById(R.id.current_page_progress_bar)
        nextButton = mainView.findViewById(R.id.next_fab)
        bottomAppBar = mainView.findViewById(R.id.bottom_bar)
        exitAllButton = mainView.findViewById(R.id.exit_out)
        tabAdapter = TabAdapter(childFragmentManager)
        tabAdapter?.addFragment(introductionFragment, getString(R.string.news_menu_title))

        CoroutineScope(Dispatchers.Main).launch {
            runOnBackgroundThread()
            runOnUIThread()
        }

        mainViewPager?.adapter = tabAdapter
        mainViewPager?.isPagingEnabled = false
        mainViewPager?.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                val valueAnimator = ValueAnimator.ofFloat(pageChangeProgressBar?.progress?.toFloat() ?: 0f,
                        (position + 1).toFloat() / tabAdapter!!.count.toFloat() * 100f)
                valueAnimator.addUpdateListener { animation: ValueAnimator ->
                    pageChangeProgressBar?.progress = (animation.animatedValue as Float).roundToInt()
                }
                valueAnimator.start()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        return mainView
    }

    companion object {
        fun newInstance(items: MutableList<QuestionnaireItem>, hashOfQuestionnaire: String): QuestionnaireFragment {
            val args = Bundle()
            val fragment = QuestionnaireFragment(items, hashOfQuestionnaire)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        questionnaireItems.removeAll(setOf<Any?>(null))
        for ((_, _, questionItems) in questionnaireItems) questionItems.removeAll(setOf<Any?>(null))
        this.questionnaireItems = questionnaireItems
        this.hashOfQuestionnaire = hashOfQuestionnaire
        resultFragment.setHashOfQuestionnaire(hashOfQuestionnaire)
    }
}