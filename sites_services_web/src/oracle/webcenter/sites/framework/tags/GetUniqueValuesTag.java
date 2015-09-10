package oracle.webcenter.sites.framework.tags;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oracle.webcenter.sites.framework.context.APIContextAware;
import oracle.webcenter.sites.framework.exceptions.APIException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetUniqueValuesTag extends TagSupport {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private String var = null;
    private String columnName = null;

    @Override
    public int doEndTag() throws JspException {
        logger.info("Entered");
        try {
            List <String> list = APIContextAware.getAuditService().getUniqueValues (getColumnName());
            pageContext.setAttribute(getVar(), list);
        } catch (APIException e) {
            throw new JspException (e);
        }
        logger.info("Leaving");
        return super.doEndTag();
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
