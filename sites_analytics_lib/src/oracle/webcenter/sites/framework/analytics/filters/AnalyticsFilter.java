package oracle.webcenter.sites.framework.analytics.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import oracle.webcenter.sites.framework.analytics.aware.AnalyticsAware;
import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;
import oracle.webcenter.sites.framework.analytics.model.Asset;
import oracle.webcenter.sites.framework.analytics.model.Payload;
import oracle.webcenter.sites.framework.analytics.model.TrackEvent;
import oracle.webcenter.sites.framework.analytics.services.AnalyticsService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

public class AnalyticsFilter implements Filter {

    private Log logger = LogFactory.getLog(getClass());
    private final String CLICKED_MAPPING = "/analytics-js/c/";
    private final String VIEWED_MAPPING = "/analytics-js/v/";

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initialising");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (logger.isInfoEnabled()) {
            logger.info("Entering");
            logger.info((new StringBuilder()).append("Servlet Path:").append(request.getServletPath()).toString());
        }
        String servletPath = request.getServletPath();
        String mapping = "/analytics-js/v/";
        String payLoadType = "Viewed";
        if (servletPath.startsWith("/analytics-js/c/")) {
            mapping = "/analytics-js/c/";
            payLoadType = "Clicked";
        }
        String jsPath = servletPath.substring(mapping.length());
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("JS Path :").append(jsPath).toString());
        String analyticsPath = jsPath.replaceAll(".js", "");
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("Analytics Path :").append(analyticsPath).toString());
        String analytics[] = analyticsPath.split("-");
        String payload_id = analytics[0];
        String timestamp = analytics[1];
        if (logger.isDebugEnabled()) {
            logger.debug("Create new Analytics [Viewed] Entry for:");
            logger.debug((new StringBuilder()).append("     Analytics Id :").append(payload_id).toString());
            logger.debug((new StringBuilder()).append("     Timestamp    :").append(timestamp).toString());
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;
        while ((str = br.readLine()) != null)
            sb.append(str);
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("     request stream :").append(sb.toString()).toString());
        try {

            JSONObject jsonObject = new JSONObject(sb.toString());
            if (jsonObject != null) {
                JSONObject analyticsData = (JSONObject) jsonObject.get("analyticsData");
                String id = String.valueOf(analyticsData.get("id"));
                String analyticsTimestamp = null;
                logger.debug((new StringBuilder()).append("Analytics Timestamp Available? :").append(analyticsData.has("timestamp")).toString());
                if (analyticsData.has("timestamp"))
                    analyticsTimestamp = String.valueOf(analyticsData.get("timestamp"));
                if (logger.isDebugEnabled()) {
                    logger.debug((new StringBuilder()).append("Payload Id :").append(id).toString());
                    logger.debug((new StringBuilder()).append("Payload timestamp :").append(analyticsTimestamp).toString());
                }
                Payload payload = new Payload(id);
                payload.setType(payLoadType);
                if (analyticsTimestamp != null && analyticsTimestamp.length() > 0)
                    payload.setTimestamp(Long.parseLong(analyticsTimestamp));
                JSONArray segments = (JSONArray) analyticsData.get("segments");
                if (segments != null && segments.length() > 0) {
                    for (int segmentIndex = 0; segmentIndex < segments.length(); segmentIndex++) {
                        JSONObject segmentJSON = segments.getJSONObject(segmentIndex);
                        String segmentId = String.valueOf(segmentJSON.get("id"));
                        String segmentType = String.valueOf(segmentJSON.get("type"));
                        if (logger.isDebugEnabled())
                            logger.debug((new StringBuilder()).append("     Segment [").append(segmentType).append(":").append(segmentId).append("]").toString());
                        Asset segment = new Asset(segmentType, segmentId, null);
                        payload.addSegment(segment);
                    }

                }
                JSONArray events = (JSONArray) analyticsData.get("events");
                if (events != null && events.length() > 0) {
                    for (int index = 0; index < events.length(); index++) {
                        JSONObject event = events.getJSONObject(index);
                        String eventId = String.valueOf(event.get("id"));
                        String eventType = String.valueOf(event.get("type"));
                        if (logger.isDebugEnabled())
                            logger.debug((new StringBuilder()).append("     Event [").append(eventType).append(":").append(eventId).append("]").toString());
                        TrackEvent trackEvent = new TrackEvent(eventType, eventId, null);
                        JSONArray assets = (JSONArray) event.get("assets");
                        if (assets != null && assets.length() > 0) {
                            for (int j = 0; j < assets.length(); j++) {
                                JSONObject assetJSON = assets.getJSONObject(j);
                                String assetId = String.valueOf(assetJSON.get("id"));
                                String assetType = String.valueOf(assetJSON.get("type"));
                                if (logger.isDebugEnabled())
                                    logger.debug((new StringBuilder()).append("     Asset [").append(assetType).append(":").append(assetId).append("]").toString());
                                Asset asset = new Asset(assetType, assetId, null);
                                trackEvent.addAsset(asset);
                            }

                        }
                        payload.addTrackEvent(trackEvent);
                    }

                }
                getAnalyticsService().createAnalyticData(payload);
            }
        } catch (Exception expGeneral) {
            new AnalyticsException(expGeneral);
        }
        String response = " ";
        OutputStream stream = servletResponse.getOutputStream();
        stream.write(response.getBytes());
        stream.close();
        if (logger.isInfoEnabled())
            logger.info("Leaving");
    }

    public void destroy() {
        logger.info("Destroying");
    }

    private AnalyticsService getAnalyticsService() {
        return AnalyticsAware.getAnalyticsService();
    }
}
