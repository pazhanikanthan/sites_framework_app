package oracle.webcenter.sites.framework.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.webcenter.sites.framework.Constants;
import oracle.webcenter.sites.framework.context.APIContextAware;
import oracle.webcenter.sites.framework.exceptions.APIException;
import oracle.webcenter.sites.framework.model.AuditReport;
import oracle.webcenter.sites.framework.model.Criteria;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExportServlet extends HttpServlet implements Constants {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entered");
        String exportType   =   getRequestParamValue (request, _EXPORTTYPE);
        String assetId      =   getRequestParamValue (request, _ASSET_ID);
        if (exportType != null) {
            try {
                Criteria criteria = null;
                if (assetId != null) {
                    criteria = new Criteria (Long.parseLong(assetId));
                }
                else {
                    criteria = new Criteria (
                                                getRequestParamValue (request, _TYPE),
                                                getRequestParamValue (request, _NAME),
                                                getRequestParamValue (request, _DEST),
                                                getRequestParamValue (request, _ST_DATE),
                                                getRequestParamValue (request, _ED_DATE)
                                            );
                }
                AuditReport report = APIContextAware.getAuditService().get(criteria);
                byte [] content = APIContextAware.getReportService(exportType).generate(report);
                response.setContentType(getMimeType(exportType));
                response.setHeader("Content-Transfer-Encoding", "binary");
                String reportFileName = ("sites_audit_report_" + System.currentTimeMillis() + "." + exportType).toLowerCase();
                response.setHeader("Content-Disposition","attachment; filename=\"" + reportFileName + "\"");
                OutputStream os = response.getOutputStream();
                byte[] buf = new byte[1024];
                InputStream is = new ByteArrayInputStream(content);
                int c = 0;
                while ((c = is.read(buf, 0, buf.length)) > 0) {
                    os.write(buf, 0, c);
                    os.flush();
                }
                os.close();
                is.close();
            } catch (APIException e) {
            }
        }
        logger.info("Leaving");
    }
    
    private String getMimeType (String type) {
        if (type.equalsIgnoreCase("PDF")) {
            return "application/pdf";
        }
        else {
            return "application/vnd.ms-excel";
        }
    }
    
    private String getRequestParamValue(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }
}
