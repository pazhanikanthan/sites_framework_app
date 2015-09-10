package oracle.webcenter.sites.framework.model;

import java.io.Serializable;

public class Link implements Serializable {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String text = null;
    private String href = null;
    private String styleClass = null;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyleClass() {
        return styleClass;
    }

}
