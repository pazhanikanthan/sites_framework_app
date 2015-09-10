package oracle.webcenter.sites.framework.analytics.taglibs;

import COM.FutureTense.Interfaces.ICS;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oracle.webcenter.sites.framework.analytics.aware.AnalyticsAware;
import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;
import oracle.webcenter.sites.framework.analytics.model.Asset;
import oracle.webcenter.sites.framework.analytics.model.FilterParams;
import oracle.webcenter.sites.framework.analytics.model.Payload;
import oracle.webcenter.sites.framework.analytics.model.TrackEvent;
import oracle.webcenter.sites.framework.analytics.model.google.AnalyticsQuery;
import oracle.webcenter.sites.framework.analytics.model.google.AnalyticsResults;
import oracle.webcenter.sites.framework.analytics.services.AnalyticsService;
import oracle.webcenter.sites.framework.analytics.services.impl.google.GoogleAnalyticsServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTagSupport extends TagSupport {

    public AbstractTagSupport() {
        c = null;
        cid = null;
        label = null;
        var = null;
        type = null;
        eventId = null;
        logger = LogFactory.getLog(getClass());
    }

    protected void createViewedPayLoad() throws AnalyticsException {
        getPayload();
    }

    protected void createClickedPayLoad(String aid) throws AnalyticsException {
        String analytics[] = aid.split("-");
        String payload_id = analytics[0];
        String eventId = analytics[1];
        String assetId = analytics[2];
        if (logger.isDebugEnabled()) {
            logger.debug("Create new Analytics [Clicked] Entry for:");
            logger.debug((new StringBuilder()).append("     Analytics Id :").append(payload_id).toString());
            logger.debug((new StringBuilder()).append("     Event     Id :").append(eventId).toString());
            logger.debug((new StringBuilder()).append("     Asset     Id :").append(assetId).toString());
        }
        Payload payload = new Payload(payload_id);
        payload.setType("Clicked");
        TrackEvent trackEvent = getAnalyticsService().populateTrackEvent(payload_id, eventId, assetId);
        payload.addTrackEvent(trackEvent);
        pageContext.getRequest().setAttribute("ANALYTICS_PAYLOAD", payload);
    }

    protected AnalyticsResults getResults(AnalyticsQuery analyticsQuery) throws AnalyticsException {
        getRequest().removeAttribute(getVar());
        AnalyticsResults analyticsResults = null;
        if (!StringUtils.isBlank(analyticsQuery.getFilters()))
            analyticsResults = getGAService().getResults(analyticsQuery);
        else
            analyticsResults = new AnalyticsResults();
        getRequest().setAttribute(getVar(), analyticsResults);
        return analyticsResults;
    }

    protected void getResults() throws AnalyticsException {
        getRequest().removeAttribute(getVar());
        oracle.webcenter.sites.framework.analytics.model.AnalyticsResult results = null;
        TrackEvent event = getTrackEvent(getC(), getCid(), getLabel());
        FilterParams filterParams = new FilterParams();
        filterParams.setEndDate(pageContext.getRequest().getParameter("endDate"));
        filterParams.setStartDate(pageContext.getRequest().getParameter("startDate"));
        filterParams.setSegments(pageContext.getRequest().getParameterValues("filterSegments"));
        results = getAnalyticsService().getResults(event, filterParams);
        getRequest().setAttribute(getVar(), results);
    }

    protected void trackEvent() throws AnalyticsException {
        TrackEvent event = getTrackEvent(getC(), getCid(), getLabel());
        getPayload().addTrackEvent(event);
    }

    protected void trackAsset() throws AnalyticsException {
        Asset asset = new Asset(getC(), getCid(), getLabel());
        TrackEvent trackEvent = getPayload().getTrackEvent(eventId);
        trackEvent.addAsset(asset);
    }

    protected void updateAssetDetail() throws AnalyticsException {
        Asset asset = new Asset(getC(), getCid(), getLabel());
        getAnalyticsService().updateAssetDetail(asset);
    }

    protected void trackSegment() throws AnalyticsException {
        Asset segment = new Asset(getC(), getCid(), getLabel());
        Payload payload = getPayload();
        logger.info((new StringBuilder()).append("Add Segment:").append(segment).toString());
        payload.addSegment(segment);
    }

    protected void setRequestAttribute(String attribName, Object value) {
        getRequest().setAttribute(attribName, value);
    }

    protected void removeRequestAttribute(String attribName) {
        getRequest().removeAttribute(attribName);
    }

    public int doAfterBody() throws JspException {
        return 0;
    }

    public int doEndTag() throws JspException {
        return 6;
    }

    protected void begin() {
        if (logger.isDebugEnabled())
            logger.debug("Entering");
    }

    protected void end() {
        if (logger.isDebugEnabled())
            logger.debug("Leaving");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getC() {
        return c;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    private AnalyticsService getAnalyticsService() {
        return AnalyticsAware.getAnalyticsService();
    }

    private GoogleAnalyticsServiceImpl getGAService() {
        return AnalyticsAware.getGAService();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    private TrackEvent getTrackEvent(String type, String id, String label) {
        return new TrackEvent(type, id, label);
    }

    protected boolean isRenderable() {
        ICS ics = (ICS) pageContext.getAttribute("ics", 1);
        String rendermode = StringUtils.defaultIfEmpty(ics.GetVar("rendermode"), "Renderable");
        return "Renderable".equalsIgnoreCase(rendermode);
    }

    private ServletRequest getRequest() {
        return pageContext.getRequest();
    }

    private Payload getPayload() {
        ServletRequest request = getRequest();
        Payload payload = null;
        if (request.getAttribute("ANALYTICS_PAYLOAD") == null) {
            logger.info("Analytic Payload is NULL");
            payload = new Payload();
            request.setAttribute("ANALYTICS_PAYLOAD", payload);
        } else {
            logger.info("Analytic Payload is NOT NULL");
            payload = (Payload) request.getAttribute("ANALYTICS_PAYLOAD");
        }
        logger.info((new StringBuilder()).append("Analytic Payload is :").append(payload).toString());
        return payload;
    }

    protected String getPayloadId() {
        if (getRequest().getAttribute("ANALYTICS_PAYLOAD") != null) {
            Payload payload = (Payload) getRequest().getAttribute("ANALYTICS_PAYLOAD");
            logger.info((new StringBuilder()).append("Analytic Payload Id is :").append(payload.getId()).toString());
            return payload.getId();
        } else {
            return null;
        }
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    private static final long serialVersionUID = 1L;
    private final String REQUEST_PAYLOAD_ATTRIBUTE = "ANALYTICS_PAYLOAD";
    private String c;
    private String cid;
    private String label;
    private String var;
    private String type;
    private String eventId;
    protected transient Log logger;
}
