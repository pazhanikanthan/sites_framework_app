package oracle.webcenter.sites.framework.utils;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.query.Query;
import com.fatwire.assetapi.query.SimpleQuery;
import com.fatwire.system.Session;
import com.fatwire.system.SessionFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import oracle.webcenter.sites.framework.model.Asset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pk.emea.qs.Utils;

public class EventsUtil {
    
    private static final Log logger = LogFactory.getLog(EventsUtil.class);
    
    private static Utils UTILS = new Utils ();
    public static final String SESSION_ATTR_EVENTS = "SESSION_ATTR_EVENTS";
    
    public static List <Asset> getEvents (HttpServletRequest request, long PARENT_ID) throws Exception {
        logger.info("Entered");
        List <Asset> events = new ArrayList <Asset> ();
        String paramEventName = request.getParameter ("eventName");
        boolean paramEventNameSet = false;
        boolean paramEventNameMatched = false;
        if (paramEventName != null && !paramEventName.trim ().equals ("")) {
            paramEventNameSet = true;
        }
        logger.debug("paramEventName:" + paramEventName);
        Iterable <AssetData> results = search();
        for( AssetData data : results ) {
            paramEventNameMatched = false;
            Asset event = new Asset (data.getAssetId().getId (), data.getAssetId().getType ());
            for( AssetId parentId : data.getParents() ) {
                if (PARENT_ID == parentId.getId ()) {
                    continue;
                }
                Map mapParent = UTILS.getAttributeData (parentId, Arrays.asList("name"), true, null, null, null);
                String parentAssetName = (String) mapParent.get("name");
                Asset parentAsset = new Asset (parentId.getId (), parentId.getType ());
                parentAsset.setName (parentAssetName);
                if (parentAssetName.equalsIgnoreCase (paramEventName)) {
                    paramEventNameMatched = true;
                }
                event.addParent (parentAsset);
            }
            if (paramEventNameSet && paramEventNameMatched) {
                List <String> eventAttributeNames = Arrays.asList("name", "createddate", "Group_category", "category", "_MEcategory", "_MEcomments", "_MEfullname", "_MEemail", "_MEphone", "_MEimageFile");
                Map eventAtributes = UTILS.getAttributeData (data.getAssetId(), eventAttributeNames, true, null, null, null);
                event.setAttributes(eventAtributes);
                events.add (event);
            }
        }
        logger.debug("events:" + events);
        logger.info("Leaving");
        return events;
    }
    
    public static List <String> getEventNames (long PARENT_ID) throws Exception {
        logger.info("Entered");
        List <String> eventNames = new ArrayList <String> ();
        Iterable <AssetData> results = search();
        for( AssetData data : results ) {
            for( AssetId parentId : data.getParents() ) {
                if (PARENT_ID == parentId.getId ()) {
                    continue;
                }
                Map mapParent = UTILS.getAttributeData (parentId, Arrays.asList("name"), true, null, null, null);
                String parentAssetName = (String) mapParent.get("name");
                if (eventNames.indexOf (parentAssetName) == -1) {
                    eventNames.add (parentAssetName);
                }
            }
        }
        logger.debug("eventNames:" + eventNames);
        logger.info("Leaving");
        return eventNames;
    }
    
    private static Iterable <AssetData> search () throws AssetAccessException {
        List<String> attributeNames = new ArrayList <String> ();
        Session ses = SessionFactory.getSession();
        AssetDataManager mgr =(AssetDataManager) ses.getManager( AssetDataManager.class.getName() );
        Query q = new SimpleQuery("QS_Content", "Attachment", null, attributeNames );
        return mgr.read( q );
    }
}
