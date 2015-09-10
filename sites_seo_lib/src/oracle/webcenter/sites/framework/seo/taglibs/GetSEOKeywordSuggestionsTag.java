package oracle.webcenter.sites.framework.seo.taglibs;

import java.util.List;

import javax.servlet.jsp.JspException;

public class GetSEOKeywordSuggestionsTag extends AbstractSEOTag {

    public GetSEOKeywordSuggestionsTag() {
        keyword = null;
        var = null;
    }

    public int doStartTag() throws JspException {
        try {
            if (logger.isDebugEnabled())
                logger.debug("doStartTag");
            List suggestions = getService().getSuggestions(getKeyword());
            if (logger.isDebugEnabled())
                logger.debug((new StringBuilder()).append("suggestions: ").append(suggestions).toString());
            pageContext.setAttribute(getVar(), suggestions);
        } catch (Exception expGeneral) {
            throw new JspException(expGeneral);
        }
        return 0;
    }

    public int doAfterBody() throws JspException {
        return 0;
    }

    public int doEndTag() {
        return 6;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    private static final long serialVersionUID = 1L;
    private String keyword;
    private String var;
}
