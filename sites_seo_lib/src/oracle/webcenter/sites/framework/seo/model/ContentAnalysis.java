package oracle.webcenter.sites.framework.seo.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class ContentAnalysis implements Serializable {

    public ContentAnalysis() {
        docScore = null;
        fleschScore = null;
        scribeScore = null;
        docScoresE = new ArrayList();
        keywords = new ArrayList();
        tags = new ArrayList();
    }

    public void setDocScore(String docScore) {
        this.docScore = docScore;
    }

    public String getDocScore() {
        return docScore;
    }

    public void setFleschScore(String fleschScore) {
        this.fleschScore = fleschScore;
    }

    public String getFleschScore() {
        return fleschScore;
    }

    public void addDocScoreE(String docScoreE) {
        docScoresE.add(docScoreE);
    }

    public void setDocScoresE(List docScores) {
        docScoresE = docScores;
    }

    public List getDocScoresE() {
        return docScoresE;
    }

    public void addKeyword(ContentAnalysisKeyword keyword) {
        keywords.add(keyword);
    }

    public void setKeywords(List keywords) {
        this.keywords = keywords;
    }

    public List getKeywords() {
        return keywords;
    }

    public void setScribeScore(String scribeScore) {
        this.scribeScore = scribeScore;
    }

    public String getScribeScore() {
        return scribeScore;
    }

    public void setTags(List tags) {
        this.tags = tags;
    }

    public List getTags() {
        return tags;
    }

    private static final long serialVersionUID = 1L;
    private String docScore;
    private String fleschScore;
    private String scribeScore;
    private List docScoresE;
    private List keywords;
    private List tags;
}
