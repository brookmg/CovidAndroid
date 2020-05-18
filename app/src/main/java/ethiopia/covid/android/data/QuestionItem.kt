package ethiopia.covid.android.data

/**
 * Created by BrookMG on 4/11/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
class QuestionItem {
    // single block question type
    var questionText: String
    var questionIconResource = 0
    var isSelectedQuestion = false
    var questionIconLink: String? = null

    constructor(questionText: String, questionIconResource: Int) {
        this.questionText = questionText
        this.questionIconResource = questionIconResource
    }

    constructor(questionText: String, questionIconLink: String?) {
        this.questionText = questionText
        this.questionIconLink = questionIconLink
    }

}