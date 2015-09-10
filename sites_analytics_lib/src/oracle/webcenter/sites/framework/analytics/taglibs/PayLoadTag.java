package oracle.webcenter.sites.framework.analytics.taglibs;

import javax.servlet.jsp.JspException;

import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;

public class PayLoadTag extends AbstractTagSupport {


    public int doStartTag() throws JspException {
        try {
            begin();
            createViewedPayLoad();
            end();
        } catch (AnalyticsException expAnalytics) {
            throw new JspException(expAnalytics);
        }
        return 1;
    }

    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    public int doAfterBody() throws JspException {
        try {
            pageContext.include("/reports/analytics/jsp/analytics.jsp");
        } catch (Exception expGeneral) {
            throw new JspException(expGeneral);
        }
        return super.doAfterBody();
    }

    private static final long serialVersionUID = 1L;
}
