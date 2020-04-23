package ethiopia.covid.android.data;

import androidx.annotation.DrawableRes;

/**
 * Created by BrookMG on 4/20/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class ContactItem {

    private int regionFlagImageLink;
    private String regionPhoneNumber;

    public ContactItem(@DrawableRes int regionFlagImageLink, String regionPhoneNumber) {
        this.regionFlagImageLink = regionFlagImageLink;
        this.regionPhoneNumber = regionPhoneNumber;
    }

    public int getRegionFlagImageLink() {
        return regionFlagImageLink;
    }

    public void setRegionFlagImageLink(int regionFlagImageLink) {
        this.regionFlagImageLink = regionFlagImageLink;
    }

    public String getRegionPhoneNumber() {
        return regionPhoneNumber;
    }

    public void setRegionPhoneNumber(String regionPhoneNumber) {
        this.regionPhoneNumber = regionPhoneNumber;
    }
}
