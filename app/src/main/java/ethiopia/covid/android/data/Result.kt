package ethiopia.covid.android.data

/**
 * Created by BrookMG on 4/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
data class Result(var questionItems: Map<QuestionnaireItem, List<QuestionItem>>,
                  var longitude: Double, var latitude: Double, var accuracy: Double)