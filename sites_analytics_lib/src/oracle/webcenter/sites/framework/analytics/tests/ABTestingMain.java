package oracle.webcenter.sites.framework.analytics.tests;

import oracle.webcenter.sites.framework.analytics.aware.AnalyticsAware;
import oracle.webcenter.sites.framework.analytics.model.AnalyticsResult;
import oracle.webcenter.sites.framework.analytics.model.FilterParams;
import oracle.webcenter.sites.framework.analytics.model.TrackEvent;
import oracle.webcenter.sites.framework.analytics.services.AnalyticsService;
import oracle.webcenter.sites.framework.analytics.utils.DatabaseUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ABTestingMain {

    public ABTestingMain() {
    }

    public static void main(String args[]) throws Exception {
        context = new FileSystemXmlApplicationContext("config/spring_analytics_AB_testing_config.xml");
        AnalyticsService service = AnalyticsAware.getAnalyticsService();
        TrackEvent event = new TrackEvent(null, "1343223926135", null);
        FilterParams filterParams = new FilterParams();
        filterParams.setStartDate("28-11-2013");
        filterParams.setEndDate("04-12-2013");
        AnalyticsResult results = service.getResults(event, filterParams);
        System.err.println((new StringBuilder()).append("Total Viewed: ").append(results.getEvent() == null ?
                                                                                 "No Events Found!!!" :
                                                                                 ((Object) (Long.valueOf(results.getEvent().getTotalViewed())))).toString());
        System.err.println((new StringBuilder()).append("Click Vs Viewed Count : ").append(results.getClickVsView() ==
                                                                                           null ?
                                                                                           "No Click Vs View Found!!!" :
                                                                                           ((Object) (Integer.valueOf(results.getClickVsView().size())))).toString());
    }

    public static DatabaseUtil getDatabaseUtil() {
        return (DatabaseUtil) getService("utils.database");
    }

    public static Object getService(String serviceName) {
        return context.getBean(serviceName);
    }

    private static ApplicationContext context = null;

}
