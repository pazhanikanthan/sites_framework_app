package oracle.webcenter.sites.framework.model;

import java.io.Serializable;

public class Attachment implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private String id = null;
    private String comment = null;
    private String event = null;
    private String picture = null;
    private String fullname = null;
    private String email = null;
    private String phone = null;

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }
    
    public String getPictureForDisplay() {
        if (picture != null) {
            return "picture size : " + picture.length();
        }
        return "Not Available";
    }
    
    public boolean isPictureAvailable() {
        if (picture != null && !picture.trim ().equals("")) {
            return true;
        }
        return false;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return  "Attachment[" + 
                "id: " + getId() + ","  +
                "event: " + getEvent() + ","  +
                "comment: " + getComment() + ","  +
                "picture: " + getPictureForDisplay() +
                "]";
    }
}
