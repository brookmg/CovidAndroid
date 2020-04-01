package ethiopia.covid.android.util;

import androidx.core.util.Pair;

import java.util.HashMap;
import java.util.Map;

import kotlin.collections.MapsKt;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.util
 * inside the project CoVidEt .
 */
public class Constant {

    public static final String TAG_HOME = "tag_home";
    public static final String PREFERENCE_THEME = "pref_theme";
    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

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
