// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   HSQLDatabaseServiceImpl.java

package oracle.webcenter.sites.framework.analytics.services.impl.hsql;

import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;
import oracle.webcenter.sites.framework.analytics.model.*;
import oracle.webcenter.sites.framework.analytics.services.DatabaseService;
import oracle.webcenter.sites.framework.analytics.utils.DatabaseUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HSQLDatabaseServiceImpl implements DatabaseService {

    public HSQLDatabaseServiceImpl() {
        logger = LogFactory.getLog(getClass());
        databaseUtil = null;
    }

    public void initialise() throws AnalyticsException {
        logger.info("Entered");
        getDatabaseUtil().initialise();
        logger.info("Leaving");
    }

    public AnalyticsResult getResults(TrackEvent event, FilterParams filterParams) throws AnalyticsException {
        logger.info("Entered");
        if (logger.isDebugEnabled())
            logger.info((new StringBuilder()).append("Event:").append(event).toString());
        AnalyticsResult results = getDatabaseUtil().getResults(event, filterParams);
        if (logger.isDebugEnabled())
            logger.info((new StringBuilder()).append("results:").append(results).toString());
        logger.info("Leaving");
        return results;
    }

    public void createAnalyticData(Payload payload) throws AnalyticsException {
        logger.info("Entered");
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("PayLoad :").append(payload).toString());
        getDatabaseUtil().createAnalyticData(payload);
        logger.info("Leaving");
    }

    public TrackEvent populateTrackEvent(String payloadId, String eventId, String assetId) throws AnalyticsException {
        return getDatabaseUtil().populateTrackEvent(payloadId, eventId, assetId);
    }

    public void updateAssetDetail(Asset asset) throws AnalyticsException {
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("Asset:").append(asset).toString());
        getDatabaseUtil().updateAssetDetail(asset);
    }

    public void setDatabaseUtil(DatabaseUtil databaseUtil) {
        this.databaseUtil = databaseUtil;
    }

    public DatabaseUtil getDatabaseUtil() {
        return databaseUtil;
    }

    private static final long serialVersionUID = 1L;
    private transient Log logger;
    private DatabaseUtil databaseUtil;
}
