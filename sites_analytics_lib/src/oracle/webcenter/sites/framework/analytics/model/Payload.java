package oracle.webcenter.sites.framework.analytics.model;

import java.io.Serializable;

import java.util.LinkedHashMap;
import java.util.Map;

import oracle.webcenter.sites.framework.analytics.utils.GUID;

public class Payload implements Serializable {

    public Payload() {
        trackEvents = new LinkedHashMap<String, TrackEvent>();
        segments = new LinkedHashMap<String, Asset>();
        id = null;
        type = "Viewed";
        timestamp = System.currentTimeMillis();
        id = (new GUID()).toString();
    }

    public Payload(String id) {
        trackEvents = new LinkedHashMap<String, TrackEvent>();
        segments = new LinkedHashMap<String, Asset>();
        this.id = null;
        type = "Viewed";
        timestamp = System.currentTimeMillis();
        this.id = id;
    }

    public String toString() {
        return (new StringBuilder()).append("[id=").append(getId()).append(",").append("trackEvents=").append(trackEvents).append(",").append("segments=").append(segments).append("]").toString();
    }

    public void addTrackEvent(TrackEvent trackEvent) {
        if (!trackEvents.containsKey(trackEvent.getId()))
            trackEvents.put(trackEvent.getId(), trackEvent);
    }

    public TrackEvent getTrackEvent(String id) {
        return trackEvents.get(id);
    }

    public Map getTrackEvents() {
        return trackEvents;
    }

    public String getId() {
        return id;
    }

    public void addSegment(Asset segment) {
        if (!segments.containsKey(segment.getId()))
            segments.put(segment.getId(), segment);
    }

    public Map getSegments() {
        return segments;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getTypeWebUrl() {
        return type.equals("Clicked") ? "c" : "v";
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    private static final long serialVersionUID = 1L;
    public static final String CLICKED = "Clicked";
    public static final String VIEWED = "Viewed";
    private Map <String, TrackEvent> trackEvents;
    private Map <String, Asset>  segments;
    private String id;
    private String type;
    private long timestamp;
}
