package oracle.webcenter.sites.framework.seo.taglibs;

import javax.servlet.jsp.JspException;

import oracle.webcenter.sites.framework.seo.model.KeywordAnalysis;

public class GetSEOKeywordAnalysisTag extends AbstractSEOTag {

    public GetSEOKeywordAnalysisTag() {
        keyword = null;
        domainVar = null;
        var = null;
    }

    public int doStartTag() throws JspException {
        try {
            if (logger.isDebugEnabled())
                logger.debug("doStartTag");
            KeywordAnalysis keywordAnalysis = getService().getKeywordAnalysis(getKeyword(), getDomainVar());
            if (logger.isDebugEnabled())
                logger.debug((new StringBuilder()).append("keywordAnalysis: ").append(keywordAnalysis).toString());
            pageContext.setAttribute(getVar(), keywordAnalysis);
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

    public void setDomainVar(String domainVar) {
        this.domainVar = domainVar;
    }

    public String getDomainVar() {
        return domainVar;
    }

    private static final long serialVersionUID = 1L;
    private String keyword;
    private String domainVar;
    private String var;
}
