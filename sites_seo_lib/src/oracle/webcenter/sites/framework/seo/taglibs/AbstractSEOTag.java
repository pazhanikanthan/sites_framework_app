package oracle.webcenter.sites.framework.seo.taglibs;

import javax.servlet.jsp.tagext.TagSupport;

import oracle.webcenter.sites.framework.seo.context.SEOContextAware;
import oracle.webcenter.sites.framework.seo.services.SEOService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractSEOTag extends TagSupport {

    public AbstractSEOTag() {
        logger = LogFactory.getLog(getClass());
    }

    protected SEOService getService() {
        return SEOContextAware.getSEOService();
    }

    private static final long serialVersionUID = 1L;
    protected transient Log logger;
}
