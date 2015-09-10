package oracle.webcenter.sites.framework.analytics.model.google;

import java.io.Serializable;

public class AnalyticsQuery implements Serializable {

    public AnalyticsQuery() {
        startDate = null;
        endDate = null;
        metrics = null;
        maxResults = null;
        sort = null;
        dimensions = null;
        pageTitle = null;
        startIndex = null;
        segment = null;
        filters = null;
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

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getSegment() {
        return segment;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getFilters() {
        return filters;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    private static final long serialVersionUID = 1L;
    private String startDate;
    private String endDate;
    private String metrics;
    private Integer maxResults;
    private String sort;
    private String dimensions;
    private String pageTitle;
    private Integer startIndex;
    private String segment;
    private String filters;
}
