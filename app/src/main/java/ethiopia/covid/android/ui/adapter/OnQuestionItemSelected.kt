package ethiopia.covid.android.ui.adapter

import ethiopia.covid.android.data.QuestionItem

/**
 * Created by BrookMG on 4/12/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
interface OnQuestionItemSelected {
    fun onItemSelected(item: QuestionItem, position: Int)
}