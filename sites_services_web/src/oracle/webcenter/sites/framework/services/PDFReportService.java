package oracle.webcenter.sites.framework.services;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

import java.util.List;

import oracle.webcenter.sites.framework.exceptions.APIException;
import oracle.webcenter.sites.framework.model.AuditRecord;
import oracle.webcenter.sites.framework.model.AuditReport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PDFReportService implements ReportService {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());

    @Override
    public byte[] generate(AuditReport auditReport) throws APIException {
        byte [] content = null;
        logger.info("Entered");
        try {
            Document document = new Document();
            ByteArrayOutputStream stream = new ByteArrayOutputStream ();
            PdfWriter.getInstance(document, stream);
            document.open();
            PdfPTable table = new PdfPTable(7);
            addHeaderCell(table, "#");
            addHeaderCell(table, "Name");
            addHeaderCell(table, "Type");
            addHeaderCell(table, "User Id");
            addHeaderCell(table, "Operation");
            addHeaderCell(table, "Destination");
            addHeaderCell(table, "Timestamp");
            
            table.getDefaultCell().setBackgroundColor(null);
            List<AuditRecord> records = auditReport.getRecords();
            for (int i = 0; i < records.size (); i++) {
                AuditRecord record = records.get(i);
                addCell(table, String.valueOf (record.getId()));
                addCell(table, record.getSourceAsset().getName());
                addCell(table, record.getSourceAsset().getType());
                addCell(table, record.getUserId());
                addCell(table, record.getOperation());
                addCell(table, ((record.getTargetAsset() != null) ? record.getTargetAsset().getName() : ""));
                addCell(table, "" + record.getTimestamp());
            }
            document.add(table);
            document.close();
            content = stream.toByteArray();
        } catch (Exception e) {
            throw new APIException ("Error generating PDF report.", e);
        }
        logger.info("Leaving");
        return content;
    }
    
    private void addCell (PdfPTable table, String text) {
        Font font = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph (text, font);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        PdfPCell cell = new PdfPCell(paragraph);
        table.addCell(cell);
    }
    
    private void addHeaderCell (PdfPTable table, String text) {
        Font font = new Font(FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph (text, font);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setBackgroundColor(BaseColor.YELLOW);
        table.addCell(cell);
    }
}
