package oracle.webcenter.sites.framework.model;

import java.io.Serializable;

public class RESTApi implements Serializable {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String path = null;
    private String method = null;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setProduces(String produces) {
        this.produces = produces;
    }

    public String getProduces() {
        return produces;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public int getResponseStatus() {
        return responseStatus;
    }
    private String produces = "application/json";
    private int responseStatus = 200;

}
