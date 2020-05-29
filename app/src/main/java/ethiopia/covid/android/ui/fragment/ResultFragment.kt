package ethiopia.covid.android.ui.fragment

import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import ethiopia.covid.android.data.QuestionItem
import ethiopia.covid.android.data.QuestionnaireItem
import ethiopia.covid.android.data.Result
import ethiopia.covid.android.databinding.ResultFragmentBinding
import ethiopia.covid.android.ui.activity.MainActivity
import ethiopia.covid.android.ui.adapter.ResultRecyclerAdapter
import ethiopia.covid.android.util.Constant.PREFERENCE_QTIME
import timber.log.Timber
import java.util.*

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class ResultFragment private constructor(
        private var questionItems: Map<QuestionnaireItem,
                List<QuestionItem>>,
        private var hashOfQuestionnaire: String) : BaseFragment() {

    private var currentLocation: Location? = null
    private lateinit var resultFragmentBinding: ResultFragmentBinding
    private val firestore = FirebaseFirestore.getInstance()

    fun setMap(items: Map<QuestionnaireItem, List<QuestionItem>>) {
        questionItems = items
        resultFragmentBinding.resultRecycler.adapter = ResultRecyclerAdapter(questionItems)
    }

    fun setCurrentLocation(currentLocation: Location?) {
        this.currentLocation = currentLocation
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
            resultFragmentBinding.resultRecycler.requestApplyInsets()
            handleWindowInsets(resultFragmentBinding.resultRecycler)
        }
    }

    fun setHashOfQuestionnaire(hashOfQuestionnaire: String) {
        this.hashOfQuestionnaire = hashOfQuestionnaire
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        resultFragmentBinding = ResultFragmentBinding.inflate(layoutInflater)
        resultFragmentBinding.resultRecycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        resultFragmentBinding.resultRecycler.adapter = ResultRecyclerAdapter(questionItems)

        resultFragmentBinding.backButton.setOnClickListener {
            if (activity is MainActivity) (activity as? MainActivity)?.callBackOnParentFragment()
        }

        resultFragmentBinding.questionnaireButton.setOnClickListener {
            val result = Result(
                    questionItems,
                    currentLocation?.longitude ?: 0.0,
                    currentLocation?.latitude ?: 0.0,
                    currentLocation?.accuracy?.toDouble() ?: 0.0
            )

            val resultJson = GsonBuilder().enableComplexMapKeySerialization()
                    .addSerializationExclusionStrategy(object : ExclusionStrategy {
                        override fun shouldSkipField(field: FieldAttributes): Boolean {
                            return field.declaringClass == QuestionnaireItem::class.java
                                    && field.name == "questionItems" || field.declaringClass == QuestionItem::class.java
                                    && field.name == "questionIconResource" || field.declaringClass == QuestionItem::class.java
                                    && field.name == "selectedQuestion" || field.declaringClass == QuestionItem::class.java
                                    && field.name == "questionIconLink"
                        }

                        override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                            return false
                        }
                    })
                    .create().toJson(result)
            val answer: MutableMap<String, Any> = HashMap()
            answer[hashOfQuestionnaire] = resultJson
            firestore
                    .collection("answers")
                    .add(answer)
                    .addOnSuccessListener { Timber.d("Added successfully.") }
            getDefaultSharedPreferences(activity).edit()
                    .putLong(PREFERENCE_QTIME, Date().time).apply()
            if (activity is MainActivity) {
                (activity as MainActivity?)!!.forcefulOnBackPressed()
            }
        }

        return resultFragmentBinding.root
    }

    companion object {
        fun newInstance(items: Map<QuestionnaireItem, List<QuestionItem>>, hashOfQuestionnaire: String): ResultFragment {
            val args = Bundle()
            val fragment = ResultFragment(items, hashOfQuestionnaire)
            fragment.arguments = args
            return fragment
        }
    }

}