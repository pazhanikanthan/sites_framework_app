package oracle.webcenter.sites.framework.types;

import com.fatwire.rest.beans.DimensionValue;


public class AttachmentAssetType extends AssetType {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String commentAttribute =   null;
    private String fullnameAttribute =   null;
    private String imageFileAttribute =   null;
    private String emailAttribute =   null;
    private String phoneAttribute =   null;

    public void setEmailAttribute(String emailAttribute) {
        this.emailAttribute = emailAttribute;
    }

    public String getEmailAttribute() {
        return emailAttribute;
    }

    public void setPhoneAttribute(String phoneAttribute) {
        this.phoneAttribute = phoneAttribute;
    }

    public String getPhoneAttribute() {
        return phoneAttribute;
    }

    public void setFullnameAttribute(String fullnameAttribute) {
        this.fullnameAttribute = fullnameAttribute;
    }

    public String getFullnameAttribute() {
        return fullnameAttribute;
    }

    private transient DimensionValue assetDimension =   null;

    public void setAssetDimension(DimensionValue assetDimension) {
        this.assetDimension = assetDimension;
    }

    public DimensionValue getAssetDimension() {
        return assetDimension;
    }

    public void setCommentAttribute(String commentAttribute) {
        this.commentAttribute = commentAttribute;
    }

    public String getCommentAttribute() {
        return commentAttribute;
    }

    public void setImageFileAttribute(String imageFileAttribute) {
        this.imageFileAttribute = imageFileAttribute;
    }

    public String getImageFileAttribute() {
        return imageFileAttribute;
    }
}
