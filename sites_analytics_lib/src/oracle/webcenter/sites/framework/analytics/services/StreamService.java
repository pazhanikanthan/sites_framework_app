package oracle.webcenter.sites.framework.analytics.services;

import java.io.Serializable;

public interface StreamService extends Serializable {

    public abstract String toXML(Object obj);

    public abstract Object toObject(String s);
}
