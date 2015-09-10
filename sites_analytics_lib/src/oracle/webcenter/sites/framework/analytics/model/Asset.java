package oracle.webcenter.sites.framework.analytics.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class Asset implements Serializable {

    public Asset(String type, String id, String label) {
        this.type = null;
        this.id = null;
        this.label = null;
        totalViewed = 0L;
        totalClicked = 0L;
        this.type = type;
        this.id = id;
        this.label = label;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return (new StringBuilder()).append("[Type=").append(type).append(",").append("Id=").append(id).append(",").append("Label=").append(label).append("]").toString();
    }

    public int hashCode() {
        return getId().hashCode();
    }

    public void setTotalViewed(long totalViewed) {
        this.totalViewed = totalViewed;
    }

    public long getTotalViewed() {
        return totalViewed;
    }

    public int getConversionRate() {
        int conversionRate = 0;
        if (totalViewed != 0L)
            conversionRate = (int) ((totalClicked * 100L) / totalViewed);
        return conversionRate;
    }

    public void setTotalClicked(long totalClicked) {
        this.totalClicked = totalClicked;
    }

    public long getTotalClicked() {
        return totalClicked;
    }

    public boolean populated() {
        return !StringUtils.isBlank(getLabel());
    }

    private static final long serialVersionUID = 1L;
    private String type;
    private String id;
    private String label;
    private long totalViewed;
    private long totalClicked;
}
