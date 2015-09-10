package oracle.webcenter.sites.framework.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.webcenter.sites.framework.EventReportService;
import oracle.webcenter.sites.framework.model.Asset;
import oracle.webcenter.sites.framework.utils.EventsUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventsDownloadServlet extends HttpServlet {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entered");
        try {
            @SuppressWarnings("unchecked")
            List<Asset> events = (List <Asset>) request.getSession().getAttribute(EventsUtil.SESSION_ATTR_EVENTS);
            
            EventReportService reportService = new EventReportService ();
            byte [] content = reportService.generate(events);
    
            response.setContentType("application/pdf");
            response.setHeader("Content-Transfer-Encoding", "binary");
            String reportFileName = ("oracle_events_report_" + System.currentTimeMillis() + ".pdf").toLowerCase();
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
        } catch (Exception e) {
            e.printStackTrace ();
            throw new ServletException (e);
        }

        logger.info("Leaving");
    }

}
