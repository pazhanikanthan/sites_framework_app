package oracle.webcenter.sites.framework.analytics.tests;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import oracle.webcenter.sites.framework.analytics.aware.AnalyticsAware;
import oracle.webcenter.sites.framework.analytics.model.google.AnalyticsQuery;
import oracle.webcenter.sites.framework.analytics.services.impl.google.GoogleAnalyticsServiceImpl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class GoogleAnalyticsServiceTest {

    public GoogleAnalyticsServiceTest() {
    }

    public static void main(String args[]) throws Exception {
        context = new FileSystemXmlApplicationContext("config/google/spring_analytics_google_config.xml");
        GoogleAnalyticsServiceImpl service = AnalyticsAware.getGAService();
        AnalyticsQuery analyticsQuery = new AnalyticsQuery();
        Date date = new Date();
        analyticsQuery.setStartDate(parseDate(date, 6));
        analyticsQuery.setEndDate(parseDate(date));
        analyticsQuery.setMetrics("ga:pageviews");
        analyticsQuery.setDimensions("ga:pageTitle,ga:week");
        analyticsQuery.setPageTitle("Home");
        analyticsQuery.setFilters("ga:pageTitle==Home");
        analyticsQuery.setMaxResults(Integer.valueOf(50));
        oracle.webcenter.sites.framework.analytics.model.google.AnalyticsResults results =
            service.getResults(analyticsQuery);
        System.out.println((new StringBuilder()).append("results: ").append(results).toString());
    }

    private static String parseDate(Date date, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, -1 * month);
        return DATE_FORMAT.format(c.getTime());
    }

    private static String parseDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    private static ApplicationContext context = null;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

}
