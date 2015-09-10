package oracle.webcenter.sites.framework.services;

import java.io.ByteArrayOutputStream;

import java.util.List;

import jxl.Workbook;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;

import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import oracle.webcenter.sites.framework.exceptions.APIException;
import oracle.webcenter.sites.framework.model.AuditRecord;
import oracle.webcenter.sites.framework.model.AuditReport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExcelReportService implements ReportService {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    @Override
    public byte[] generate(AuditReport auditReport) throws APIException {
        byte [] content = null;
        logger.info("Entered");
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream ();
            WritableWorkbook workbook = Workbook.createWorkbook(stream);
            
            WritableSheet sheet2 = workbook.createSheet("Report", 0);
            // Header Row
            createLabel(sheet2, 0, 0, "#", Alignment.CENTRE);
            createLabel(sheet2, 1, 0, "Name", Alignment.LEFT);
            createLabel(sheet2, 2, 0, "Type", Alignment.LEFT);
            createLabel(sheet2, 3, 0, "User Id", Alignment.CENTRE);
            createLabel(sheet2, 4, 0, "Operation", Alignment.CENTRE);
            createLabel(sheet2, 5, 0, "Destination", Alignment.LEFT);
            createLabel(sheet2, 6, 0, "Timestamp", Alignment.LEFT);
            
            List<AuditRecord> records = auditReport.getRecords();
            for (int i = 0; i < records.size (); i++) {
                AuditRecord record = records.get(i);
                createValue(sheet2, 0, (i+1), String.valueOf (record.getId()), Alignment.CENTRE);
                createValue(sheet2, 1, (i+1), record.getSourceAsset().getName(), Alignment.LEFT);
                createValue(sheet2, 2, (i+1), record.getSourceAsset().getType(), Alignment.LEFT);
                createValue(sheet2, 3, (i+1), record.getUserId(), Alignment.CENTRE);
                createValue(sheet2, 4, (i+1), record.getOperation(), Alignment.CENTRE);
                createValue(sheet2, 5, (i+1), ((record.getTargetAsset() != null) ? record.getTargetAsset().getName() : ""), Alignment.LEFT);
                createValue(sheet2, 6, (i+1), "" + record.getTimestamp(), Alignment.LEFT);
            }
            
            workbook.write();
            workbook.close(); 
            content = stream.toByteArray();
        } catch (Exception e) {
            throw new APIException ("Error generating XLS report.", e);
        }
        logger.info("Leaving");
        return content;
    }
    
    private void createValue (WritableSheet sheet, int column, int row, String text, Alignment alignment) throws WriteException {
        addCell(sheet, column, row, 10, text, alignment, false, false);
    }
    
    private void createLabel (WritableSheet sheet, int column, int row, String text, Alignment alignment) throws WriteException {
        addCell(sheet, column, row, 10, text, alignment, true, true);
    }
    
    private void createSummaryHeadline (WritableSheet sheet, int column, int row, String text, Alignment alignment) throws WriteException {
        addCell(sheet, column, row, 20, text, alignment, true, true);
    }
    
    private void addCell (WritableSheet sheet, int column, int row, int fontSize, String text, Alignment alignment, boolean isBold, boolean background) throws WriteException {
        WritableFont rowFont = new WritableFont (WritableFont.ARIAL, fontSize);
        if (isBold) {
            rowFont.setBoldStyle(WritableFont.BOLD);
        }
        WritableCellFormat cellFormat = new WritableCellFormat(rowFont);
        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        cellFormat.setAlignment(alignment);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        if (background) {
            cellFormat.setBackground(Colour.YELLOW2);
        }
        sheet.addCell(new Label(column, row, text, cellFormat));
    }
    
    
}
