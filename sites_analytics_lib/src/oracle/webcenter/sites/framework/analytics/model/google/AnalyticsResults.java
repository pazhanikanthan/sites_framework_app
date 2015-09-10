package oracle.webcenter.sites.framework.analytics.model.google;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsResults implements Serializable {

    public AnalyticsResults() {
        pageTitle = null;
        empty = true;
        headers = new ArrayList<String>();
        data = new ArrayList <AnalyticsData> ();
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void addHeader(String header) {
        headers.add(header);
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List getHeaders() {
        return headers;
    }

    public void setData(List<AnalyticsData> data) {
        this.data = data;
    }

    public void addData(AnalyticsData analyticsData) {
        data.add(analyticsData);
    }

    public List <AnalyticsData> getData() {
        return data;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    private static final long serialVersionUID = 1L;
    private String pageTitle;
    private boolean empty;
    private List<String> headers;
    private List<AnalyticsData>  data;
}
