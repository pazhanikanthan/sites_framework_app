package oracle.webcenter.sites.framework.analytics.taglibs;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.jsp.JspException;

import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;
import oracle.webcenter.sites.framework.analytics.model.google.AnalyticsQuery;

import org.apache.commons.lang.StringUtils;

public class GetGAResultsTag extends AbstractTagSupport {

    public GetGAResultsTag() {
        pageTitle = null;
        dimensions = null;
        metrics = null;
        maxResults = 50;
        months = 6;
    }

    public int doStartTag() throws JspException {
        begin();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug((new StringBuilder()).append("pageTitle : ").append(getPageTitle()).toString());
                logger.debug((new StringBuilder()).append("metrics   : ").append(getMetrics()).toString());
                logger.debug((new StringBuilder()).append("dimensions: ").append(getDimensions()).toString());
                logger.debug((new StringBuilder()).append("maxResults: ").append(getMaxResults()).toString());
            }
            AnalyticsQuery analyticsQuery = new AnalyticsQuery();
            Date date = new Date();
            analyticsQuery.setStartDate(parseDate(date, getMonths()));
            analyticsQuery.setEndDate(parseDate(date));
            analyticsQuery.setMetrics(getMetrics());
            analyticsQuery.setDimensions(getDimensions());
            if (!StringUtils.isBlank(getPageTitle())) {
                analyticsQuery.setPageTitle(getPageTitle());
                analyticsQuery.setFilters((new StringBuilder()).append("ga:pageTitle==").append(getPageTitle()).toString());
            }
            else {
                analyticsQuery.setFilters(getFilters());
            }
            analyticsQuery.setMaxResults(Integer.valueOf(getMaxResults()));
            getResults(analyticsQuery);
        } catch (AnalyticsException e) {
            throw new JspException(e);
        }
        end();
        return 0;
    }

    private String parseDate(Date date, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, -1 * month);
        return DATE_FORMAT.format(c.getTime());
    }

    private String parseDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getMonths() {
        return months;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getFilters() {
        return filters;
    }

    private static final long serialVersionUID = 1L;
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private String pageTitle;
    private String dimensions;
    private String filters;
    private String metrics;
    private int maxResults;
    private int months;
}
