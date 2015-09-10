package oracle.webcenter.sites.framework.model;

import java.io.Serializable;

import java.util.Date;

public class AuditRecord implements Serializable {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private long id = -1L;
    private String userId = null;
    private String operation = null;
    private Asset sourceAsset = null;
    private Asset targetAsset = null;
    private Date timestamp = null;
    
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setSourceAsset(Asset sourceAsset) {
        this.sourceAsset = sourceAsset;
    }

    public Asset getSourceAsset() {
        return sourceAsset;
    }

    public void setTargetAsset(Asset targetAsset) {
        this.targetAsset = targetAsset;
    }

    public Asset getTargetAsset() {
        return targetAsset;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return  "operation:" + operation + "," +
                "userId:" + userId + "," +
                "sourceAsset:" + sourceAsset + "," +
                "targetAsset:" + targetAsset;
    }
}
