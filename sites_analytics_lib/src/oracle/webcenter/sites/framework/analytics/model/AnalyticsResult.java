package oracle.webcenter.sites.framework.analytics.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsResult implements Serializable {

    public AnalyticsResult() {
        recordFound = false;
        event = null;
        segments = new ArrayList<Segment>();
        clickVsView = new ArrayList<ClickVsView>();
    }

    public void setEvent(TrackEvent event) {
        this.event = event;
    }

    public TrackEvent getEvent() {
        return event;
    }

    public void addSegment(Segment segment) {
        segments.add(segment);
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public List getSegments() {
        return segments;
    }

    public void setRecordFound(boolean recordFound) {
        this.recordFound = recordFound;
    }

    public boolean isRecordFound() {
        return recordFound;
    }

    public String toString() {
        return (new StringBuilder()).append("[recordFound=").append(recordFound).append(",").append("event=").append(event).append(",").append("segments=").append(segments).append("]").toString();
    }

    public void addClickVsView(ClickVsView clickVsViewObj) {
        clickVsView.add(clickVsViewObj);
    }

    public void setClickVsView(List<ClickVsView> clickVsView) {
        this.clickVsView = clickVsView;
    }

    public List getClickVsView() {
        return clickVsView;
    }

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    private boolean recordFound;
    private TrackEvent event;
    private List<Segment> segments;
    private List<ClickVsView> clickVsView;
}
