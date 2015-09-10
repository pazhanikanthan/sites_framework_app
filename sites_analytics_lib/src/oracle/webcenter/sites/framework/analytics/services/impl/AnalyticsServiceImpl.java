// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   AnalyticsServiceImpl.java

package oracle.webcenter.sites.framework.analytics.services.impl;

import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;
import oracle.webcenter.sites.framework.analytics.model.*;
import oracle.webcenter.sites.framework.analytics.services.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AnalyticsServiceImpl implements AnalyticsService {

    public AnalyticsServiceImpl() {
        streamService = null;
        databaseService = null;
        logger = LogFactory.getLog(getClass());
    }

    public void initialise() throws AnalyticsException {
        logger.info("Entered");
        getDatabaseService().initialise();
        logger.info("Leaving");
    }

    public void updateAssetDetail(Asset asset) throws AnalyticsException {
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("Asset:").append(asset).toString());
        getDatabaseService().updateAssetDetail(asset);
    }

    public AnalyticsResult getResults(TrackEvent event, FilterParams filterParams) throws AnalyticsException {
        AnalyticsResult results = null;
        logger.info((new StringBuilder()).append("Event:").append(event).toString());
        results = getDatabaseService().getResults(event, filterParams);
        return results;
    }

    public void createAnalyticData(Payload payload) throws AnalyticsException {
        logger.info("Creating Analytic Data in Database!");
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("payload :").append(payload).toString());
        getDatabaseService().createAnalyticData(payload);
    }

    public TrackEvent populateTrackEvent(String payloadId, String eventId, String assetId) throws AnalyticsException {
        return getDatabaseService().populateTrackEvent(payloadId, eventId, assetId);
    }

    public void setStreamService(StreamService streamService) {
        this.streamService = streamService;
    }

    public StreamService getStreamService() {
        return streamService;
    }

    public void setDatabaseService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    private static final long serialVersionUID = 1L;
    private StreamService streamService;
    private DatabaseService databaseService;
    private transient Log logger;
}
