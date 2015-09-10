package oracle.webcenter.sites.framework.seo.services;

import java.io.Serializable;

import java.util.List;

import oracle.webcenter.sites.framework.seo.model.ContentAnalysis;
import oracle.webcenter.sites.framework.seo.model.KeywordAnalysis;

public interface SEOService extends Serializable {

    public abstract List getSuggestions(String s);

    public abstract KeywordAnalysis getKeywordAnalysis(String s, String s1);

    public abstract ContentAnalysis getContentAnalysis(String s, String s1, String s2, String s3, String s4);
}
