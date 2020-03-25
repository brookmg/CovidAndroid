package ethiopia.covid.android.data;

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class DetailItem {

    private String title;
    private String imageLink;
    private boolean hasMoreButton;
    private String text;
    private int imageResource;

    public DetailItem(String title, int imageResource) {
        this(title, imageResource, false, "");
    }

    public DetailItem(String title, int imageResource, boolean hasMoreButton, String text) {
        this(title, "", hasMoreButton, text, imageResource);
    }

    public DetailItem(String title, String imageLink) {
        this(title, imageLink, false, "");
    }

    public DetailItem(String title, String imageLink, boolean hasMoreButton, String text) {
        this(title, imageLink, hasMoreButton, text, -1);
    }

    public DetailItem(String title, String imageLink, boolean hasMoreButton, String text, int imageResource) {
        this.title = title;
        this.imageLink = imageLink;
        this.hasMoreButton = hasMoreButton;
        this.text = text;
        this.imageResource = imageResource;
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
}
