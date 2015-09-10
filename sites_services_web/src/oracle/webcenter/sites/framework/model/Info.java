package oracle.webcenter.sites.framework.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Info implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String version = null;
    private List <String> authors = new ArrayList <String> ();
    private Date lastUpdatedDate = new Date ();


    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void addAuthor(String author) {
        this.authors.add (author);
    }
    
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }
}
