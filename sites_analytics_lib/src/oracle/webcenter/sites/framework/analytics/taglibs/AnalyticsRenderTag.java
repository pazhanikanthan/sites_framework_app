package oracle.webcenter.sites.framework.analytics.taglibs;

import javax.servlet.jsp.JspException;

public class AnalyticsRenderTag extends AbstractTagSupport {

    public AnalyticsRenderTag() {
    }

    public int doStartTag() throws JspException {
        begin();
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("Renderable ?: ").append(isRenderable()).toString());
        if (!isRenderable()) {
            return 0;
        } else {
            end();
            return 1;
        }
    }

    private static final long serialVersionUID = 1L;
}
