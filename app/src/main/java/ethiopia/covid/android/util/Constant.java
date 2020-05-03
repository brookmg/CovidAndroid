package ethiopia.covid.android.util;

import androidx.core.util.Pair;

import com.franmontiel.localechanger.LocaleChanger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kotlin.collections.MapsKt;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.util
 * inside the project CoVidEt .
 */
public class Constant {

    public static final String TAG_HOME = "tag_home";
    public static final String TAG_QUESTIONNAIRE = "tag_questionnaire";
    public static final String TAG_HEAT_MAP = "tag_heat_map";

    public static final String PREFERENCE_THEME = "pref_theme";
    public static final String PREFERENCE_LATEST_NEWS = "pref_latest_news";
    public static final String PREFERENCE_QTIME = "pref_q_time";
    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String FRC_QUESTIONNAIRE_EN = "frc_questionnaire_en";
    public static final String FRC_QUESTIONNAIRE_AM = "frc_questionnaire_am";
    public static final String FRC_QUESTIONNAIRE_OR = "frc_questionnaire_or";
    public static final String FRC_QUESTIONNAIRE_TI = "frc_questionnaire_ti";

    public static String getQuestionnaireConstant(String localeLanguage) {
        return "frc_questionnaire_" + localeLanguage;
    }

    public static final Map<String, String> regionNameWithCodeMap = new HashMap<String, String>() {
        {
            put("Addis Ababa", "aa");
            put("Afar Region", "af");
            put("Amhara Region", "am");
            put("Benishangul-Gumuz Region", "be");
            put("Dire Dawa", "dd");
            put("Gambela Region", "ga");
            put("Harari Region", "ha");
            put("Oromia Region", "or");
            put("Somali Region", "so");
            put("Southern Nations, Nationalities, and Peoples' Region", "snnp");
            put("Tigray Region", "tg");
        }
    };
    
    public static final Map<String, String> statusIdentifierMap = new HashMap<String, String>() {
        {
            put("ST" , "Stable");
            put("CR" , "Critical");
            put("RE" , "Recovered");
            put("NA" , "Not Available");
            put("DC" , "Deceased");
        }
    };

}
