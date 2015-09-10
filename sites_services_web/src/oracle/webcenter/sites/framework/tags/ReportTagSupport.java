package oracle.webcenter.sites.framework.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oracle.webcenter.sites.framework.Constants;
import oracle.webcenter.sites.framework.context.APIContextAware;
import oracle.webcenter.sites.framework.exceptions.APIException;
import oracle.webcenter.sites.framework.model.AuditReport;
import oracle.webcenter.sites.framework.model.Criteria;
import oracle.webcenter.sites.framework.model.Link;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReportTagSupport extends TagSupport implements Constants {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private String var = null;

    @Override
    public int doStartTag() throws JspException {
        logger.info("Entered");
        try {
            Criteria criteria = null;
            if (getAssetId() != null) {
                criteria = new Criteria (Long.parseLong(getAssetId()));
            }
            else {
                criteria = new Criteria (
                                            getRequestParamValue (_TYPE),
                                            getRequestParamValue (_NAME),
                                            getRequestParamValue (_DEST),
                                            getRequestParamValue (_ST_DATE),
                                            getRequestParamValue (_ED_DATE)
                                        );
            }
            int totalRecordCount = APIContextAware.getAuditService().count(criteria);
            criteria = populateStartAndEndRowNum (totalRecordCount, getRecordsPerPage(), getPageNum(), criteria);
            AuditReport report = APIContextAware.getAuditService().get(criteria);
            populateRecordsPerPage(criteria, report);
            populatePages (criteria,report);
            populateExportOptions (criteria,report);
            pageContext.setAttribute(getVar(), report);
        } catch (APIException e) {
            throw new JspException (e);
        }
        logger.info("Leaving");
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        logger.info("Entered");
        logger.info("Leaving");
        return super.doEndTag();
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }
    
    private int getRecordsPerPage() {
        int recordsPerPage = 25;
        String _rpp = pageContext.getRequest().getParameter(_RPP);
        if (_rpp != null) {
            recordsPerPage = Integer.parseInt (_rpp);
        }
        return recordsPerPage;
    }
    
    private int getPageNum() {
        int pageNumber = 1;
        String _pnum = pageContext.getRequest().getParameter(_PNUM);
        if (_pnum != null) {
            pageNumber = Integer.parseInt (_pnum);
        }
        return pageNumber;
    }
    
    private String getAssetId() {
        return pageContext.getRequest().getParameter(_ASSET_ID);
    }
    
    private String getRequestParamValue(String paramName) {
        return pageContext.getRequest().getParameter(paramName);
    }

    private void populateRecordsPerPage(Criteria criteria, AuditReport report) {
        int [] rppArray = new int [] {25, 50, 100};
        for (int i = 0; i < rppArray.length; i++) {
            int rpp = rppArray [i];
            Link recordPerPage = new Link ();
            recordPerPage.setText(String.valueOf(rpp));
            if (rpp == getRecordsPerPage()) {
                recordPerPage.setStyleClass("active");
            }
            recordPerPage.setHref(getRecordPerPageHref (criteria, rpp));
            report.addRecordPerPage(recordPerPage);
        }
    }

    private void populatePages(Criteria criteria, AuditReport report) {
        int totalRecordCount = report.getTotalRecordCount();
        int rpp = getRecordsPerPage();
        if (totalRecordCount > 0) {
            int pageCount = totalRecordCount / rpp;
            if ((totalRecordCount % rpp) != 0) {
                pageCount++;
            }
            for (int i = 1; i <= pageCount; i++) {
                Link page = new Link ();
                page.setText(String.valueOf(i));
                page.setHref(getPageHref (criteria, i));
                if (i == getPageNum()) {
                    page.setStyleClass("active");
                }
                report.addPage(page);
            }
        }
    }
    
    private void populateExportOptions(Criteria criteria, AuditReport report) {
        Link exportOption = new Link ();
        exportOption.setText("pdf");
        exportOption.setHref(getExportHref(criteria, exportOption.getText()));
        report.addExportOption(exportOption);
        exportOption = new Link ();
        exportOption.setText("xls");
        exportOption.setHref(getExportHref(criteria, exportOption.getText()));
        report.addExportOption(exportOption);
    }
    
    private String getExportHref (Criteria criteria, String exportType) {
        StringBuilder builder = new StringBuilder ();
        builder.append("export?");
        builder.append(_EXPORTTYPE + "=" + exportType);
        buildQueryParam(builder, criteria);
        return builder.toString ();
    }
    
    private String getRecordPerPageHref (Criteria criteria, int rpp) {
        StringBuilder builder = new StringBuilder ();
        builder.append("?");
        builder.append(_RPP + "=" + rpp);
        buildQueryParam(builder, criteria);
        return builder.toString ();
    }
    
    private String getPageHref (Criteria criteria, int pageNum) {
        final String AMPERSAND = "&";
        StringBuilder builder = new StringBuilder ();
        builder.append("?");
        builder.append(_RPP + "=" + getRecordsPerPage());
        builder.append(AMPERSAND);
        builder.append(_PNUM + "=" + pageNum);
        buildQueryParam(builder, criteria);
        return builder.toString ();
    }
    
    
    
    private void buildQueryParam (StringBuilder builder, Criteria criteria) {
        final String AMPERSAND = "&";
        if (criteria.isAssetSearch()) {
            builder.append(AMPERSAND);
            builder.append(_ASSET_ID + "=" + getAssetId());
        }
        else {
            if (!isBlank(criteria.getName())) {
                builder.append(AMPERSAND);
                builder.append(_NAME + "=" + criteria.getName());
            }
            if (!isBlank(criteria.getDestination())) {
                builder.append(AMPERSAND);
                builder.append(_DEST + "=" + criteria.getDestination());
            }
            if (!isBlank(criteria.getType())) {
                builder.append(AMPERSAND);
                builder.append(_TYPE + "=" + criteria.getType());
            }
            if (!isBlank(criteria.getStartDate())) {
                builder.append(AMPERSAND);
                builder.append(_ST_DATE + "=" + criteria.getStartDate());
            }
            if (!isBlank(criteria.getEndDate())) {
                builder.append(AMPERSAND);
                builder.append(_ED_DATE + "=" + criteria.getEndDate());
            }
        }
    }

    private Criteria populateStartAndEndRowNum(int totalRecordCount, int rpp, int pnum, Criteria criteria) {
        int startRowNum = 0; 
        int endRowNum = 0;
        if (totalRecordCount > 0) {
            startRowNum = (pnum * rpp) - (rpp) + 1;
            endRowNum = (pnum * rpp);
            if (totalRecordCount < endRowNum) {
                endRowNum = totalRecordCount;
            }
            criteria.setStartAndEndRowNum(startRowNum, endRowNum);
        }
        return criteria;
    }
    
    private boolean isBlank (String value) {
        if (value != null && !value.trim().equals("")) {
            return false;
        }
        return true;
    }


}
