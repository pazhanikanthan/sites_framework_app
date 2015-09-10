package oracle.webcenter.sites.framework.analytics.model;

import java.io.Serializable;

public class FilterParams implements Serializable {

    public FilterParams() {
        segments = null;
        startDate = null;
        endDate = null;
    }

    public boolean isSegmentFilterSelected() {
        return segments != null && segments.length > 0;
    }

    public boolean isStartDateFilterSelected() {
        return startDate != null && startDate.length() > 0;
    }

    public boolean isEndDateFilterSelected() {
        return endDate != null && endDate.length() > 0;
    }

    public void setSegments(String segments[]) {
        this.segments = segments;
    }

    public String[] getSegments() {
        return segments;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    private static final long serialVersionUID = 1L;
    private String segments[];
    private String startDate;
    private String endDate;
}
