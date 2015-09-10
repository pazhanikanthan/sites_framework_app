package oracle.webcenter.sites.framework.analytics.services;

import java.io.Serializable;

import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;
import oracle.webcenter.sites.framework.analytics.model.AnalyticsResult;
import oracle.webcenter.sites.framework.analytics.model.Asset;
import oracle.webcenter.sites.framework.analytics.model.FilterParams;
import oracle.webcenter.sites.framework.analytics.model.Payload;
import oracle.webcenter.sites.framework.analytics.model.TrackEvent;

public interface AnalyticsService extends Serializable {

    public abstract void initialise() throws AnalyticsException;

    public abstract void updateAssetDetail(Asset asset) throws AnalyticsException;

    public abstract AnalyticsResult getResults(TrackEvent trackevent, FilterParams filterparams) throws AnalyticsException;

    public abstract void createAnalyticData(Payload payload) throws AnalyticsException;

    public abstract TrackEvent populateTrackEvent(String s, String s1, String s2) throws AnalyticsException;
}
