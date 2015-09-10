package oracle.webcenter.sites.framework.analytics.model;

import java.io.Serializable;

import java.util.Date;

public class ClickVsView implements Serializable {

    public ClickVsView() {
        clickCount = 0L;
        viewCount = 0L;
        date = null;
    }

    public void addClickCount() {
        clickCount++;
    }

    public long getClickCount() {
        return clickCount;
    }

    public void addViewCount() {
        viewCount++;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    private static final long serialVersionUID = 1L;
    private long clickCount;
    private long viewCount;
    private Date date;
}
