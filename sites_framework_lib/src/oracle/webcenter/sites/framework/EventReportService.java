package oracle.webcenter.sites.framework;

import com.fatwire.assetapi.data.BlobObject;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.webcenter.sites.framework.model.Asset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventReportService implements Serializable {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    public byte [] generate (List<Asset> events) throws Exception {
        logger.info("Entered");
        ByteArrayOutputStream stream = new ByteArrayOutputStream ();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, stream);
            document.open();
            for (Asset event : events) {
                PdfPTable table = new PdfPTable(2);
                addHeaderCell(table, "Event Name:");
                addCell(table, event.getParents().get(0).getName());
                addHeaderCell(table, "Full Name:");
                addCell(table, (String) event.getAttributes().get("_MEfullname"));
                addHeaderCell(table, "Email:");
                addCell(table, (String) event.getAttributes().get("_MEemail"));
                addHeaderCell(table, "Phone:");
                addCell(table, (String) event.getAttributes().get("_MEphone"));
                addHeaderCell(table, "Comments:");
                addCell(table, (String) event.getAttributes().get("_MEcomments"));
                addHeaderCell(table, "Submitted Date:");
                addCell(table, "" + (Date) event.getAttributes().get("createddate"));
                addHeaderCell(table, "Attachment:");
                BlobObject blob = (BlobObject) event.getAttributes().get("_MEimageFile");
                if (blob != null) {
                    Image img = Image.getInstance (IOUtils.toByteArray(blob.getBinaryStream()));
                    img.scalePercent(50);
                    addImageCell(table, img);
                }
                else {
                    addCell(table, "Not Available");
                }
                document.add(table);
                document.newPage();
            }
            document.close();
            
        } catch (Exception e) {
            e.printStackTrace ();
            throw e;
        }

        logger.info("Leaving");
        return stream.toByteArray();
    }

    private void addCell (PdfPTable table, String text) {
        Font font = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph (text, font);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        PdfPCell cell = new PdfPCell(paragraph);
        table.addCell(cell);
    }
    
    private void addImageCell (PdfPTable table, Image img) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Chunk(img, 5, -5));
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
    
    public static void main (String a[]) throws Exception {
        List<Asset> events = new ArrayList <Asset> ();
        for (int i = 1; i <= 10; i++) {
            Asset asset = new Asset ();
            asset.setName("Asset Name " + i);
            asset.getAttributes().put("_MEfullname", "Paz " + i);
            asset.getAttributes().put("_MEemail", "paz.periasamy" + i + "@oracle.com");
            asset.getAttributes().put("_MEphone", "041135483" + i);
            asset.getAttributes().put("_MEcomments", "Comment " + i);
            asset.getAttributes().put("createddate", new Date ());
            if ((i % 2) == 0 ) {
                asset.getAttributes().put("_MEimageFile", "D:/temp/Attachment-1.jpg");
            }
            Asset parentAsset = new Asset ();
            parentAsset.setName("NSW Health Expo");
            asset.addParent(parentAsset);
            events.add(asset);
        }
        EventReportService service = new EventReportService ();
        byte [] content = service.generate(events);
        IOUtils.write(content, new FileOutputStream ("D:/temp/test.pdf"));
    }
}
