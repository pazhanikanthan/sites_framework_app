package oracle.webcenter.sites.framework.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Asset implements Serializable {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private long id = -1L;
    private String type = null;
    private String name = null;
    private Map attributes = new LinkedHashMap ();
    private List<Asset> parents = new ArrayList <Asset> ();
    
    public Asset() {
    }

    public Asset(long id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public Asset(long id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addParent(Asset parent) {
        this.parents.add(parent);
    }
    
    public void setParents(List<Asset> parents) {
        this.parents = parents;
    }

    public List<Asset> getParents() {
        return parents;
    }

    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }

    public Map getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return  "[" +
                "id:" + id + "," +
                "type:" + type + "," +
                "attributes:" + attributes + "," +
                "name:" + name + "]";
    }
}
