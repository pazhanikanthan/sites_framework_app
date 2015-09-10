package oracle.webcenter.sites.framework.analytics.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrackEvent extends Asset implements Serializable {

    public TrackEvent(String type, String id, String label) {
        super(type, id, label);
        totalViewed = 0L;
        assets = new ArrayList();
    }

    public boolean isTopConversionRenderable() {
        boolean renderable = false;
        int TOP_CONV_COUNT = 5;
        int loopIndex = 1;
        Iterator i$ = assets.iterator();
        do {
            if (!i$.hasNext())
                break;
            Asset asset = (Asset) i$.next();
            if (asset.getTotalClicked() <= 0L)
                continue;
            renderable = true;
            break;
        } while (++loopIndex <= 5);
        return renderable;
    }

    public String toString() {
        return (new StringBuilder()).append("[assets=").append(assets).append("]").append(super.toString()).toString();
    }

    public int hashCode() {
        return getId().hashCode();
    }

    public void addAsset(Asset asset) {
        if (assets.indexOf(asset) == -1)
            assets.add(asset);
    }

    public void setAssets(List assets) {
        this.assets = assets;
    }

    public List getAssets() {
        return assets;
    }

    public void setTotalViewed(long totalViewed) {
        this.totalViewed = totalViewed;
    }

    public long getTotalViewed() {
        return totalViewed;
    }

    private static final long serialVersionUID = 1L;
    private long totalViewed;
    private List assets;
}
