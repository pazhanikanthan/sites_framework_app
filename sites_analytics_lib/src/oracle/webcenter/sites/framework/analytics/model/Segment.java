package oracle.webcenter.sites.framework.analytics.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Segment extends Asset implements Serializable {

    public Segment(String type, String id, String label) {
        super(type, id, label);
        assets = new ArrayList<Asset>();
        timeSeries = new LinkedHashMap<String, Long> ();
        selected = true;
    }

    public Segment(Asset asset) {
        super(asset.getType(), asset.getId(), asset.getLabel());
        assets = new ArrayList<Asset>();
        timeSeries = new LinkedHashMap<String, Long> ();
        selected = true;
        super.setTotalClicked(asset.getTotalClicked());
        super.setTotalViewed(asset.getTotalViewed());
    }

    public void addAsset(Asset asset) {
        if (assets.indexOf(asset) == -1)
            assets.add(asset);
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public List getAssets() {
        return assets;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setTimeSeriesIndicator(String dateAsString, String segmentId) {
        Long counter = Long.valueOf(0L);
        if (timeSeries.containsKey(dateAsString))
            counter = (Long) timeSeries.get(dateAsString);
        if (segmentId.equalsIgnoreCase(getId())) {
            Long long1 = counter;
            Long long2 = counter = Long.valueOf(counter.longValue() + 1L);
            Long _tmp = long1;
        }
        timeSeries.put(dateAsString, counter);
    }

    public Map getTimeSeries() {
        return timeSeries;
    }

    private static final long serialVersionUID = 1L;
    private List <Asset> assets;
    private Map <String, Long> timeSeries;
    private boolean selected;
}
