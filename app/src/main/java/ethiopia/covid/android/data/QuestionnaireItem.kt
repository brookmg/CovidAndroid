package ethiopia.covid.android.data

import java.io.Serializable

/**
 * Created by BrookMG on 4/12/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
data class QuestionnaireItem(
        var questionType: QuestionType,
        var questionText: String,
        var questionItems: List<QuestionItem>
) : Serializable {
    enum class QuestionType { SINGLE_BLOCK_QUESTION, SINGLE_CHOICE_QUESTION, SINGLE_MULTIPLE_CHOICE_QUESTION }
}