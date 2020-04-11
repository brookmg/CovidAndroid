package ethiopia.covid.android.data;

import java.util.List;

/**
 * Created by BrookMG on 4/10/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class ProtectiveMeasures {

    private String title;
    private String source;
    private List<String> content;

    public ProtectiveMeasures(String title, String source, List<String> content) {
        this.title = title;
        this.source = source;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
