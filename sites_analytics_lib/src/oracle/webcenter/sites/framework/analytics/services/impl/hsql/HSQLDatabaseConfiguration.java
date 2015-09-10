// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   HSQLDatabaseConfiguration.java

package oracle.webcenter.sites.framework.analytics.services.impl.hsql;

import java.io.Serializable;

public class HSQLDatabaseConfiguration implements Serializable {

    public HSQLDatabaseConfiguration() {
        databaseUrl = null;
        schemaFilePath = null;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public void setSchemaFilePath(String schemaFilePath) {
        this.schemaFilePath = schemaFilePath;
    }

    public String getSchemaFilePath() {
        return schemaFilePath;
    }

    private static final long serialVersionUID = 1L;
    private String databaseUrl;
    private String schemaFilePath;
}
