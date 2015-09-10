package oracle.webcenter.sites.framework.types;

import java.io.Serializable;

public abstract class AssetType implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID          =   1L;
    
    private String type                 =   null;
    private String subType              =   null;
    private String parentDefName        =   null;
    private String parentAssetRef       =   null;
    private String template             =   null;
    private String templateAttribute    =   null;

    public void setTemplateAttribute(String templateAttribute) {
        this.templateAttribute = templateAttribute;
    }

    public String getTemplateAttribute() {
        return templateAttribute;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public String getType() {
        return type;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubType() {
        return subType;
    }

    public void setParentDefName(String parentDefName) {
        this.parentDefName = parentDefName;
    }

    public String getParentDefName() {
        return parentDefName;
    }

    public void setParentAssetRef(String parentAssetRef) {
        this.parentAssetRef = parentAssetRef;
    }

    public String getParentAssetRef() {
        return parentAssetRef;
    }

}
