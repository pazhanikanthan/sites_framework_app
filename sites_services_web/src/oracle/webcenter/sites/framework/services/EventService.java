package oracle.webcenter.sites.framework.services;

import com.fatwire.rest.beans.AssetBean;
import com.fatwire.rest.beans.Attribute;
import com.fatwire.rest.beans.Blob;
import com.fatwire.rest.beans.Parent;
import com.fatwire.wem.sso.SSOException;
import com.fatwire.wem.sso.SSOSession;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import oracle.webcenter.sites.framework.exceptions.APIException;
import oracle.webcenter.sites.framework.model.Attachment;
import oracle.webcenter.sites.framework.types.AttachmentAssetType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;


/**

    Don't know about the new code (11.1.1.8.0) but this old (11.1.1.6.0) code used doesn't cope with the CSRF protection token and the new version of Sites added a CSRF protection filter, so we can either handle the token from the code (as used in the new code) :

        //Add the CSRF header (between steps 6 & 7).
        builder = builder.header("X-CSRF-Token", multiticket);

    The 11.1.1.8.0 code location

        WCS_Sites/misc/Samples/WEM Samples/REST API samples/Flex Assets/com/fatwire/rest

or add the rest request patterns to the filter exceptions list in ReqAuthConfig.xml file in /WEB-INF/classes folder

    <property name="excludedURLs">
        <!--  URLs in this section would be excluded from CSRF protection-->
        <!--  For example to exclude rest calls add <value>/REST/**</value> in the list-->
       <list>
        <value>/ContentServer?[pagename=fatwire/wem/sso/ssoLogin|OpenMarket/Xcelerate/Actions/ProcessQueue|OpenMarket/Xcelerate/Actions/CleanTempObjects|OpenMarket/Xcelerate/Search/Event,#]</value>
        <value>/CatalogManager?[ftcmd=login,#]</value>
        <value>/FlushServer</value>
        <value>/CacheServer</value>
        <value>/cachetool/inventory.jsp</value>
        <value>/Inventory</value>
      </list>
    </property>

        // Troubleshooting UniformInterfaceException
        // =========================================
        //
        // Cause: HTTP 403: User does not have permission to access the REST
        // resource.
        // Remedy: Use an authorized CAS user to use this REST resource.
        //
        // Cause: HTTP 404: Either site and/or asset type does not exist, or the
        // asset type is not enabled in the site.
        // Remedy: Verify existence of the site and/or asset type, if necessary
        // create the site and/or type and make sure that the type is enabled in
        // the site.
        //
        // Cause: HTTP 500: Generic server side exception.
        // Remedy: Verify that the source AssetBean has been provided with all
        // mandatory attributes, associations as per type definition, verify
        // that at least one site has been provided in the publist attribute of
        // the AssetBean.
        //
        // Cause: UnmarshalException.
        // Remedy: Verify that the correct bean has been provided in the
        // argument for put().
 */


public class EventService implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private String restUrl                      =   null;
    private String username                     =   null;
    private String password                     =   null;
    private String ssoTicket                    =   null;
    private String siteid                       =   null;
    private transient SSOSession ssoSession     =   null;
    private AttachmentAssetType attachmentAssetType       =   new AttachmentAssetType ();    
    
    public void create (Attachment attachmentAsset) throws APIException {
        logger.info("Entered");
        try {
            logger.debug("attachment:" + attachmentAsset);
            Client client           =   Client.create();
            String sitesUrl         =   createSitesAssetURL (getAttachmentAssetType().getType(), getSiteid());
            WebResource webResource =   createWebResource(client, sitesUrl);
            Builder builder         =   webResource.accept(MediaType.APPLICATION_XML);
            
            // Instantiate and define the AssetBean for the asset
            AssetBean sourceAsset = new AssetBean();
            
            // Name - mandatory field
            sourceAsset.setName("Attachment-" + System.currentTimeMillis());
            
            // Asset Dimension
            sourceAsset.getDimensions().add(getAttachmentAssetType().getAssetDimension());
           
            // Add Attribute 'template'
            sourceAsset.getAttributes().add(createStringAttribute(getAttachmentAssetType().getTemplateAttribute(), getAttachmentAssetType().getTemplate()));
            
            // Add Attribute '_MEfullname'
            if (!StringUtils.isBlank(attachmentAsset.getFullname())) {
                sourceAsset.getAttributes().add(createStringAttribute(getAttachmentAssetType().getFullnameAttribute(), attachmentAsset.getFullname()));
            }
            // Add Attribute '_MEemail'
            if (!StringUtils.isBlank(attachmentAsset.getEmail())) {
                sourceAsset.getAttributes().add(createStringAttribute(getAttachmentAssetType().getEmailAttribute(), attachmentAsset.getEmail()));
            }
            // Add Attribute '_MEphone'
            if (!StringUtils.isBlank(attachmentAsset.getPhone())) {
                sourceAsset.getAttributes().add(createStringAttribute(getAttachmentAssetType().getPhoneAttribute(), attachmentAsset.getPhone()));
            }

            // Add Attribute '_MEcomments'
            sourceAsset.getAttributes().add(createStringAttribute(getAttachmentAssetType().getCommentAttribute(), attachmentAsset.getComment()));
            
            if (attachmentAsset.isPictureAvailable()) {
                // Convert Base64 String to byte []
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] imageByte = decoder.decodeBuffer(attachmentAsset.getPicture());
                
                // Add Attribute 'imageFile'
                sourceAsset.getAttributes().add(createBlobAttribute(getAttachmentAssetType().getImageFileAttribute(), imageByte, sourceAsset.getName() + ".jpg"));
            }
            
            // publist - add the site id this asset need to be visible
            sourceAsset.getPublists().add(getSiteid());
            
            // subtype - set the subtype for this asset
            sourceAsset.setSubtype(getAttachmentAssetType().getSubType());
            
            // Set Asset's Parent Information
            Parent parent = new Parent();
            parent.setParentDefName(getAttachmentAssetType().getParentDefName());
            parent.getAssets().add(attachmentAsset.getEvent());
            sourceAsset.getParents().add(parent);
            
            AssetBean resultAsset = builder.put(AssetBean.class, sourceAsset);
            attachmentAsset.setId(resultAsset.getId());
            
            logger.debug ("Created the attachment asset : " + attachmentAsset);
            
            if (client != null) {
                client.destroy();
            }
        }
        catch (Exception expGeneral) {
            throw new APIException ("Exception creating event attachment record " + attachmentAsset, expGeneral);
        }
        logger.info("Leaving");
    }

    private String createSitesAssetURL (String assetType, String siteid) {
        return getRestUrl() + "/sites/" + siteid + "/types/" + assetType + "/assets/0";
    }
    
    private String getSitesAssetURL (String assetType, String siteid, String assetId) {
        return getRestUrl() + "/sites/" + siteid + "/types/" + assetType + "/assets/" + assetId;
    }
    
    private String getSitesAssetSearchURL (String assetType, String siteid) {
        return getRestUrl() + "/sites/" + siteid + "/types/" + assetType + "/search";
    }
        
    private String createAssetTypesURL () {
        return getRestUrl() + "/types";
    }

    private WebResource createWebResource (Client client, String url) throws APIException {
        WebResource webResource =   client.resource( url );
        webResource             =   webResource.queryParam("multiticket", getSsoTicket());
        webResource.header("Pragma", "auth-redirect=false");
        return webResource;
    }
    
    private Attribute createStringAttribute (String attributeName, String attributeValue) {
        // Set Attribute Name
        Attribute attribute = new Attribute();
        attribute.setName(attributeName);
        
        // Set Attribute Data
        Attribute.Data data = new Attribute.Data();
        data.setStringValue(attributeValue);
        attribute.setData(data);
        
        return attribute;
    }
    
    private Attribute createDateAttribute (String attributeName, Date attributeValue) {
        // Set Attribute Name
        Attribute attribute = new Attribute();
        attribute.setName(attributeName);
        
        // Set Attribute Data
        Attribute.Data data = new Attribute.Data();
        data.setDateValue(attributeValue);
        attribute.setData(data);
        
        return attribute;
    }
    
    private Attribute createListAttribute (String attributeName, List<String> attributeValues) {
        // Set Attribute Name
        Attribute attribute = new Attribute();
        attribute.setName(attributeName);
        
        // Set Attribute Data
        Attribute.Data data = new Attribute.Data();
        data.getStringLists().addAll(attributeValues);
        attribute.setData(data);
        
        return attribute;
    }
    
    
    private Attribute createBlobAttribute (String attributeName, byte [] attributeValue, String attributeFileName) {
        // Set Attribute Name
        Attribute attribute = new Attribute();
        attribute.setName(attributeName);
        
        // Set Attribute Data
        Attribute.Data data = new Attribute.Data();
        Blob blob = new Blob ();
        blob.setFiledata(attributeValue);
        blob.setFilename(attributeFileName);

        data.setBlobValue(blob);
        attribute.setData(data);
        
        return attribute;
    }

    private String getSsoTicket () throws APIException {
        if (ssoTicket == null) {
            try {
                ssoTicket = getSsoSession().getMultiTicket(getUsername(), getPassword());
            }
            catch (SSOException expSSO) {
                throw new APIException ("Cannot get SSO Ticket.", expSSO);
            }
        }
        return ssoTicket;
    }


    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }

    public String getRestUrl() {
        return restUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSsoTicket(String ssoTicket) {
        this.ssoTicket = ssoTicket;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setAttachmentAssetType(AttachmentAssetType attachmentAssetType) {
        this.attachmentAssetType = attachmentAssetType;
    }

    public AttachmentAssetType getAttachmentAssetType() {
        return attachmentAssetType;
    }

    public void setSsoSession(SSOSession ssoSession) {
        this.ssoSession = ssoSession;
    }

    public SSOSession getSsoSession() {
        return ssoSession;
    }


}
