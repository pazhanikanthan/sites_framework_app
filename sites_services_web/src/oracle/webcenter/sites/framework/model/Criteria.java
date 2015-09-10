package oracle.webcenter.sites.framework.model;

import java.io.Serializable;

public class Criteria implements Serializable {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private long assetId = 0L;
    private int startRowNum = 0;
    private int endRowNum = 0;
    private boolean startAndEndRowNumSet = false;
    private boolean assetSearch = false;
    

    private String type         =   null;
    private String name         =   null;
    private String destination  =   null;
    private String startDate    =   null;
    private String endDate      =   null;
    
    public Criteria (long assetId) {
        this.assetId = assetId;
        assetSearch = true;
    }

    public Criteria(String type, String name, String destination, String startDate, String endDate) {
        this.type = type;
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        assetSearch = false;
    }

    public boolean isAssetSearch() {
        return assetSearch;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDestination() {
        return destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getStartRowNum() {
        return startRowNum;
    }

    public boolean isStartAndEndRowNumSet() {
        return startAndEndRowNumSet;
    }

    public void setStartAndEndRowNum(int startRowNum, int endRowNum) {
        this.startRowNum = startRowNum;
        this.endRowNum = endRowNum;
        startAndEndRowNumSet = true;
    }

    public int getEndRowNum() {
        return endRowNum;
    }

    public long getAssetId() {
        return assetId;
    }

}
