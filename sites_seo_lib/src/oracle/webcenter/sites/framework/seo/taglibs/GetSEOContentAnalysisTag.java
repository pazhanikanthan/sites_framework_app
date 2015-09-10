package oracle.webcenter.sites.framework.seo.taglibs;

import javax.servlet.jsp.JspException;

import oracle.webcenter.sites.framework.seo.model.ContentAnalysis;

public class GetSEOContentAnalysisTag extends AbstractSEOTag {

    public GetSEOContentAnalysisTag() {
        htmlTitleVar = null;
        htmlDescriptionVar = null;
        htmlHeadlineVar = null;
        htmlBodyVar = null;
        domainVar = null;
        var = null;
    }

    public int doStartTag() throws JspException {
        try {
            if (logger.isDebugEnabled())
                logger.debug("doStartTag");
            String htmlTitle = getHtmlTitleVar();
            String htmlDescription = getHtmlDescriptionVar();
            String htmlHeadline = getHtmlHeadlineVar();
            String htmlBody = getHtmlBodyVar();
            String domain = getDomainVar();
            ContentAnalysis contentAnalysis =
                getService().getContentAnalysis(htmlTitle, htmlDescription, htmlHeadline, htmlBody, domain);
            if (logger.isDebugEnabled())
                logger.debug((new StringBuilder()).append("contentAnalysis: ").append(contentAnalysis).toString());
            pageContext.setAttribute(getVar(), contentAnalysis);
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

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    public void setHtmlTitleVar(String htmlTitleVar) {
        this.htmlTitleVar = htmlTitleVar;
    }

    public String getHtmlTitleVar() {
        return htmlTitleVar;
    }

    public void setHtmlDescriptionVar(String htmlDescriptionVar) {
        this.htmlDescriptionVar = htmlDescriptionVar;
    }

    public String getHtmlDescriptionVar() {
        return htmlDescriptionVar;
    }

    public void setHtmlHeadlineVar(String htmlHeadlineVar) {
        this.htmlHeadlineVar = htmlHeadlineVar;
    }

    public String getHtmlHeadlineVar() {
        return htmlHeadlineVar;
    }

    public void setHtmlBodyVar(String htmlBodyVar) {
        this.htmlBodyVar = htmlBodyVar;
    }

    public String getHtmlBodyVar() {
        return htmlBodyVar;
    }

    public void setDomainVar(String domainVar) {
        this.domainVar = domainVar;
    }

    public String getDomainVar() {
        return domainVar;
    }

    private static final long serialVersionUID = 1L;
    private String htmlTitleVar;
    private String htmlDescriptionVar;
    private String htmlHeadlineVar;
    private String htmlBodyVar;
    private String domainVar;
    private String var;
}
