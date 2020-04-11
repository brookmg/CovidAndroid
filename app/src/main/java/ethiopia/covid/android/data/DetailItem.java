package ethiopia.covid.android.data;

import android.view.View;

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class DetailItem {

    private String title;
    private String imageLink;
    private boolean hasMoreButton;
    private String text;
    private int imageResource = -1;
    private View.OnClickListener moreButtonClickListener;

    private FAQ.QuestionItem faqItem = null;

    public DetailItem(String title, int imageResource) {
        this(title, imageResource, false, "", null);
    }

    public DetailItem(String title, int imageResource, boolean hasMoreButton, String text, View.OnClickListener moreButtonClickListener) {
        this(title, "", hasMoreButton, text, imageResource, moreButtonClickListener);
    }

    public DetailItem(String title, String imageLink) {
        this(title, imageLink, false, "", null);
    }

    public DetailItem(String title, String imageLink, boolean hasMoreButton, String text, View.OnClickListener moreButtonClickListener) {
        this(title, imageLink, hasMoreButton, text, -1, moreButtonClickListener);
    }

    public DetailItem(String title, String imageLink, boolean hasMoreButton, String text, int imageResource, View.OnClickListener moreButtonClickListener) {
        this.title = title;
        this.imageLink = imageLink;
        this.hasMoreButton = hasMoreButton;
        this.text = text;
        this.imageResource = imageResource;
        this.moreButtonClickListener = moreButtonClickListener;
    }

    public DetailItem(FAQ.QuestionItem faqItem) {
        this.faqItem = faqItem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public boolean isHasMoreButton() {
        return hasMoreButton;
    }

    public void setHasMoreButton(boolean hasMoreButton) {
        this.hasMoreButton = hasMoreButton;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public View.OnClickListener getMoreButtonClickListener() {
        return moreButtonClickListener;
    }

    public void setMoreButtonClickListener(View.OnClickListener moreButtonClickListener) {
        this.moreButtonClickListener = moreButtonClickListener;
    }

    public FAQ.QuestionItem getFaqItem() {
        return faqItem;
    }

    public void setFaqItem(FAQ.QuestionItem faqItem) {
        this.faqItem = faqItem;
    }
}
