package oracle.webcenter.sites.framework.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oracle.webcenter.sites.framework.utils.EventsUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetEventNamesTagSupport extends TagSupport {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    private transient Log logger = LogFactory.getLog(this.getClass());

    private String varEventNames = null;
    private long parentId = 0L;

    @Override
    public int doStartTag() throws JspException {
        logger.info("Entered");
        try {
            pageContext.setAttribute(getVarEventNames(), EventsUtil.getEventNames(getParentId()));
        } catch (Exception e) {
            throw new JspException (e);
        }
        logger.info("Leaving");
        return super.doStartTag();
    }
    
    public void setVarEventNames(String varEventNames) {
        this.varEventNames = varEventNames;
    }

    public String getVarEventNames() {
        return varEventNames;
    }
    
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getParentId() {
        return parentId;
    }
}
