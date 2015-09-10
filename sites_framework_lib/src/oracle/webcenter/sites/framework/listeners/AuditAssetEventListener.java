package oracle.webcenter.sites.framework.listeners;

import com.fatwire.assetapi.data.AssetId;
import com.fatwire.cs.core.event.EventException;

import com.openmarket.basic.event.AbstractAssetEventListener;
import com.openmarket.basic.event.AssetEvent;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.Map;
import java.util.ResourceBundle;

import oracle.webcenter.sites.framework.model.Asset;
import oracle.webcenter.sites.framework.model.AuditRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @param assetId
 */
public class AuditAssetEventListener extends AbstractAssetEventListener {

    protected static final Log log = LogFactory.getLog(AuditAssetEventListener.class);
    
    private static final String PROP_USER = "user";
    private static final String PROP_TARGET_ID = "targetId";
    
    private static String baseUrl = null;
    private static String serviceUrl = null;
    private static String restBaseUrl = null;
    
    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("satellite");
        baseUrl         =   resourceBundle.getString ("protocol") + resourceBundle.getString ("host") + ":" + resourceBundle.getString ("port");
        serviceUrl      =   baseUrl + resourceBundle.getString ("service");
        restBaseUrl     =   baseUrl + "/services/rest";
    }

    /**
     *
     * @param assetId
     */
    @Override
    public void assetAdded(AssetId assetId) {
        log.info("Entered");
        log.info("Leaving");
    }

    /**
     * @param assetId
     */
    @Override
    public void assetUpdated(AssetId assetId) {
        log.info("Entered");
        log.info("Leaving");
    }

    @Override
    public void onEvent(AssetEvent assetEvent) throws EventException {
        log.info("Entered");
        if (AssetEvent.OP_TYPE.DELETE != assetEvent.getOp()) {
            try {
                AuditRecord auditRecord = createAuditRecord(assetEvent.getId(), assetEvent.getAttributes(), assetEvent.getOp());
                populateAsset(auditRecord.getSourceAsset());
                if (auditRecord.getTargetAsset() != null) {
                    populateAsset(auditRecord.getTargetAsset());
                }
                auditRecord (auditRecord);
            }
            catch (Throwable throwable) {
                log.info("throwable:" + throwable);
                throwable.printStackTrace();
            }
        }
        log.info("Leaving");
        super.onEvent(assetEvent);
    }

    /**
     * @param assetId
     */
    @Override
    public void assetDeleted(AssetId assetId) {
        log.info("Entered");
        log.info("Leaving");
    }
    
    private AuditRecord createAuditRecord (AssetId assetId, Map<String, Object> properties, AssetEvent.OP_TYPE operation) {
        AuditRecord auditRecord = new AuditRecord ();
        auditRecord.setOperation(operation.toString());
        auditRecord.setSourceAsset(new Asset (assetId.getId(), assetId.getType()));
        if (isValidValue(PROP_USER, properties)) {
            auditRecord.setUserId((String) properties.get(PROP_USER));
        }
        if (isValidValue(PROP_TARGET_ID, properties)) {
            auditRecord.setTargetAsset(new Asset ((Long) properties.get(PROP_TARGET_ID), "PubTarget"));
        }
        return auditRecord;
    }
    
    private void auditRecord (AuditRecord auditRecord) {
        log.info("Entered");
        log.info("auditRecord: " + auditRecord);
        String resourceUrl = restBaseUrl + "/audit/create";
        log.info("resourceUrl: " + resourceUrl);
        Client client = Client.create();
        WebResource res = client.resource(resourceUrl);
        WebResource.Builder resBuilder = res.accept("application/json");
        resBuilder = resBuilder.type("application/json");
        ClientResponse resp = resBuilder.post(ClientResponse.class, auditRecord);
        log.info("auditRecord created.");
        log.info("Leaving");
    }
    
    private Asset populateAsset (Asset asset) {
        log.info("Entered");
        log.info("asset: " + asset);
        try {
            Client client = Client.create();
            String resourceUrl = serviceUrl + "?pagename=feature-audit-get-attributes&c=" + asset.getType() + "&cid=" + asset.getId();
            log.info("resourceUrl: " + resourceUrl);
            WebResource res = client.resource(resourceUrl);
            WebResource.Builder resBuilder = res.accept("application/json");
            resBuilder = resBuilder.type("application/json");
            ClientResponse resp = resBuilder.get(ClientResponse.class);
            String response = resp.getEntity(String.class);
            log.info("response :" + response);
            JSONObject data = new JSONObject(response);
            asset.setName( (String) data.get("name"));
        } catch (JSONException e) {
        }
        log.info("asset: " + asset);
        log.info("Leaving");
        return asset;
    }
    
    private boolean isValidValue (String propName, Map<String, Object> properties) {
        if (properties.containsKey(propName) && properties.get(propName) != null) {
            return true;
        }
        return false;
    }
    
}
