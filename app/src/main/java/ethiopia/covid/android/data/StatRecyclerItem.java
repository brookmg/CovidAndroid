package ethiopia.covid.android.data;

import java.util.List;

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class StatRecyclerItem {

    private int type = 0;  // 0 -> Table , 1 -> Pie

    // Table stuff
    private List<CovidStatItem> tableItems;
    private List<String> headers;
    private int fixedHeaderCount = 0;

    // todo: Pie stuff

    public StatRecyclerItem(List<CovidStatItem> tableItems, List<String> headers) {
        this(0, tableItems, headers, 0);
    }

    public StatRecyclerItem(int type, List<CovidStatItem> tableItems, List<String> headers) {
        this(type, tableItems, headers, 0);
    }

    public StatRecyclerItem(int type, List<CovidStatItem> tableItems, List<String> headers, int fixedHeaderCount) {
        this.type = type;
        this.tableItems = tableItems;
        this.headers = headers;
        this.fixedHeaderCount = fixedHeaderCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<CovidStatItem> getTableItems() {
        return tableItems;
    }

    public void setTableItems(List<CovidStatItem> tableItems) {
        this.tableItems = tableItems;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public int getFixedHeaderCount() {
        return fixedHeaderCount;
    }

    public void setFixedHeaderCount(int fixedHeaderCount) {
        this.fixedHeaderCount = fixedHeaderCount;
    }
}
