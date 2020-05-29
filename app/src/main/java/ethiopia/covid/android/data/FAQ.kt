package ethiopia.covid.android.data

/**
 * Created by BrookMG on 4/10/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
data class FAQ(var source: String, var data: List<QuestionItem>) {
    data class QuestionItem(var question: String, var answer: String)
}