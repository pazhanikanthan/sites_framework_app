package oracle.webcenter.sites.framework.seo.tests;

import oracle.webcenter.sites.framework.seo.context.SEOContextAware;
import oracle.webcenter.sites.framework.seo.services.SEOService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ScribeServiceTest {

    public ScribeServiceTest() {
    }

    public static void main(String args[]) throws Exception {
        context = new FileSystemXmlApplicationContext("config/spring_seo_config.xml");
        String keyword = "Sports";
        String domain = "www.oracle.com";
        String htmlTitle = "This is a Page Title";
        String htmlDescription = "Description of the Page";
        String htmlHeadline = "Headline of Page";
        String htmlBody =
            "Body within the Page INFO: Refreshing org.springframework.context.support.FileSystemXmlApplicationContext@a7ef2e: display name [org.springframework.context.support.FileSystemXmlApplicationContext@a7ef2e]; startup date [Wed May 14 22:38:36 EST 2014]; root of context hierarchy";
        oracle.webcenter.sites.framework.seo.model.ContentAnalysis contentAnalysis =
            getService().getContentAnalysis(htmlTitle, htmlDescription, htmlHeadline, htmlBody, domain);
        System.out.println((new StringBuilder()).append("Content Analysis : ").append(contentAnalysis).toString());
    }

    private static SEOService getService() {
        return SEOContextAware.getSEOService();
    }

    private static ApplicationContext context = null;

}
