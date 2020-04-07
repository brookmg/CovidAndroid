package ethiopia.covid.android;

import org.junit.Test;
import ethiopia.covid.android.util.Utils;
import static org.junit.Assert.assertEquals;

public class UtilsUnitTest {
    @Test
    public void number_correctly_formatted() {
        long numbers[] = { 1000 , 102344, 22 , 494, 1000000000 };
        String formatted[] = { "1,000" , "102,344" , "22" , "494" , "1,000,000,000" };

        for (int i = 0; i < numbers.length; i++) {
            assertEquals("Testing " + numbers[i], formatted[i] , Utils.formatNumber(numbers[i]));
        }
    }
}