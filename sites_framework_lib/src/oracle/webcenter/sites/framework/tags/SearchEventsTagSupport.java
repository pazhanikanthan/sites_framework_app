package oracle.webcenter.sites.framework.tags;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oracle.webcenter.sites.framework.model.Asset;
import oracle.webcenter.sites.framework.utils.EventsUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SearchEventsTagSupport extends TagSupport {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    private transient Log logger = LogFactory.getLog(this.getClass());

    private String varAssetList = null;
    private long parentId = 0L;

    @Override
    public int doStartTag() throws JspException {
        logger.info("Entered");
        try {
            List<Asset> events = EventsUtil.getEvents((HttpServletRequest) pageContext.getRequest(), getParentId());
            pageContext.setAttribute(getVarAssetList(), events);
            pageContext.getSession().setAttribute(EventsUtil.SESSION_ATTR_EVENTS, events);
            
        } catch (Exception e) {
            throw new JspException (e);
        }
        logger.info("Leaving");
        return super.doStartTag();
    }

    public void setVarAssetList(String varAssetList) {
        this.varAssetList = varAssetList;
    }

    public String getVarAssetList() {
        return varAssetList;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getParentId() {
        return parentId;
    }

}
