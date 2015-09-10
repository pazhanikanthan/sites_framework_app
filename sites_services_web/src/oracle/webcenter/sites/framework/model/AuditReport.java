package oracle.webcenter.sites.framework.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class AuditReport implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private List<AuditRecord> records = new ArrayList<AuditRecord> ();
    
    private int totalRecordCount = 0;
    
    private List<Link> recordsPerPage = new ArrayList<Link> ();
    private List<Link> pages = new ArrayList<Link> ();
    private List<Link> exportOptions = new ArrayList<Link> ();

    public void addExportOption(Link exportOption) {
        this.exportOptions.add (exportOption);
    }
    
    public void setExportOptions(List<Link> exportOptions) {
        this.exportOptions = exportOptions;
    }

    public List<Link> getExportOptions() {
        return exportOptions;
    }

    public void setRecords(List<AuditRecord> records) {
        this.records = records;
    }

    public List<AuditRecord> getRecords() {
        return records;
    }

    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    public void addRecordPerPage(Link recordPerPage) {
        this.recordsPerPage.add (recordPerPage);
    }
    
    public void setRecordsPerPage(List<Link> recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public List<Link> getRecordsPerPage() {
        return recordsPerPage;
    }

    public void addPage(Link page) {
        this.pages.add (page);
    }
    
    public void setPages(List<Link> pages) {
        this.pages = pages;
    }

    public List<Link> getPages() {
        return pages;
    }
}
