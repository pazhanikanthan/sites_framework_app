package oracle.webcenter.sites.framework.analytics.taglibs;

import javax.servlet.jsp.JspException;

import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;

public class GetAnalyticsResultsTag extends AbstractTagSupport {

    public GetAnalyticsResultsTag() {
    }

    public int doStartTag() throws JspException {
        begin();
        try {
            getResults();
        } catch (AnalyticsException e) {
            throw new JspException(e);
        }
        end();
        return 0;
    }

    private static final long serialVersionUID = 1L;
}
