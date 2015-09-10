package oracle.webcenter.sites.framework.seo.context;

import oracle.webcenter.sites.framework.seo.services.SEOService;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SEOContextAware implements ApplicationContextAware {


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SEOContextAware.applicationContext = applicationContext;
    }

    public static SEOService getSEOService() {
        return (SEOService) getService("services.seo.scribe");
    }

    private static Object getService(String serviceName) {
        return applicationContext.getBean(serviceName);
    }

    private static ApplicationContext applicationContext = null;

}
