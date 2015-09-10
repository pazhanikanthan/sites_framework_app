// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   XStreamServiceImpl.java

package oracle.webcenter.sites.framework.analytics.services.impl.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import oracle.webcenter.sites.framework.analytics.services.StreamService;

public class XStreamServiceImpl implements StreamService {

    public XStreamServiceImpl() {
    }

    public String toXML(Object object) {
        return xstream.toXML(object);
    }

    public Object toObject(String xml) {
        return xstream.fromXML(xml);
    }

    private static final long serialVersionUID = 1L;
    private static XStream xstream = new XStream(new DomDriver());

}
