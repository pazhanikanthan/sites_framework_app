package oracle.webcenter.sites.framework.analytics.utils;

import java.util.Comparator;

import oracle.webcenter.sites.framework.analytics.model.Asset;

public class AssetComparator implements Comparator {

    public AssetComparator() {
    }

    public int compare(Object object1, Object object2) {
        Asset asset1 = (Asset) object1;
        Asset asset2 = (Asset) object2;
        return asset2.getConversionRate() - asset1.getConversionRate();
    }
}
