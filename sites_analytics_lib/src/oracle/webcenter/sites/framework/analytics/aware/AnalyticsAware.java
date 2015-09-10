package oracle.webcenter.sites.framework.analytics.aware;

import oracle.webcenter.sites.framework.analytics.services.AnalyticsService;
import oracle.webcenter.sites.framework.analytics.services.impl.google.GoogleAnalyticsServiceImpl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AnalyticsAware implements ApplicationContextAware {

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (AnalyticsAware.applicationContext == null) {
            AnalyticsAware.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return AnalyticsAware.applicationContext;
    }

    public static Object getService(String serviceName) {
        return AnalyticsAware.applicationContext.getBean(serviceName);
    }

    public static AnalyticsService getAnalyticsService() {
        return (AnalyticsService) getService("services.analytics");
    }

    public static GoogleAnalyticsServiceImpl getGAService() {
        return (GoogleAnalyticsServiceImpl) getService("services.analytics.google");
    }

    private static ApplicationContext applicationContext = null;

}
