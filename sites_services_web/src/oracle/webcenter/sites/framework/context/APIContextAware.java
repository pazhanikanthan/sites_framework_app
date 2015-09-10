package oracle.webcenter.sites.framework.context;

import oracle.webcenter.sites.framework.services.AuditService;
import oracle.webcenter.sites.framework.services.EventService;
import oracle.webcenter.sites.framework.services.ReportService;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class APIContextAware implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;
         
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APIContextAware.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static AuditService getAuditService () {
        return (AuditService) applicationContext.getBean("services.audit");
    }
    
    public static EventService getEventService () {
        return (EventService) applicationContext.getBean("services.event");
    }
    
    public static ReportService getReportService (String type) {
        return (ReportService) applicationContext.getBean("services.report." + type);
    }
}
