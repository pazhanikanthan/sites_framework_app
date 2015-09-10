package oracle.webcenter.sites.framework.servlets;

import org.springframework.web.servlet.DispatcherServlet;

public class AuditDispatcherServlet extends DispatcherServlet {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    static {
        if (System.getProperty("weblogic.Name") != null) {
            System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
            System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
        }
    }


}
