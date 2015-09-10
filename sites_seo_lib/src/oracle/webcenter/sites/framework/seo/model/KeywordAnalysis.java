package oracle.webcenter.sites.framework.seo.model;

import java.io.Serializable;

public class KeywordAnalysis implements Serializable {

    public KeywordAnalysis() {
        scoreDifficulty = null;
        scoreContent = null;
        scoreLinks = null;
        scoreFacebookLikes = null;
        scoreGenderMale = null;
        scoreGenderFemale = null;
        agePrimaryDescription = null;
        agePrimaryValue = null;
        ageSecondaryDescription = null;
        ageSecondaryValue = null;
        ppc = null;
        volumeAnnual = null;
        volumeMonthly = null;
    }

    public void setScoreDifficulty(String scoreDifficulty) {
        this.scoreDifficulty = scoreDifficulty;
    }

    public String getScoreDifficulty() {
        return scoreDifficulty;
    }

    public void setScoreContent(String scoreContent) {
        this.scoreContent = scoreContent;
    }

    public String getScoreContent() {
        return scoreContent;
    }

    public void setScoreLinks(String scoreLinks) {
        this.scoreLinks = scoreLinks;
    }

    public String getScoreLinks() {
        return scoreLinks;
    }

    public void setScoreFacebookLikes(String scoreFacebookLikes) {
        this.scoreFacebookLikes = scoreFacebookLikes;
    }

    public String getScoreFacebookLikes() {
        return scoreFacebookLikes;
    }

    public void setScoreGenderMale(String scoreGenderMale) {
        this.scoreGenderMale = scoreGenderMale;
    }

    public String getScoreGenderMale() {
        return scoreGenderMale;
    }

    public void setScoreGenderFemale(String scoreGenderFemale) {
        this.scoreGenderFemale = scoreGenderFemale;
    }

    public String getScoreGenderFemale() {
        return scoreGenderFemale;
    }

    public void setAgePrimaryDescription(String agePrimaryDescription) {
        this.agePrimaryDescription = agePrimaryDescription;
    }

    public String getAgePrimaryDescription() {
        return agePrimaryDescription;
    }

    public void setAgePrimaryValue(String agePrimaryValue) {
        this.agePrimaryValue = agePrimaryValue;
    }

    public String getAgePrimaryValue() {
        return agePrimaryValue;
    }

    public void setAgeSecondaryDescription(String ageSecondaryDescription) {
        this.ageSecondaryDescription = ageSecondaryDescription;
    }

    public String getAgeSecondaryDescription() {
        return ageSecondaryDescription;
    }

    public void setAgeSecondaryValue(String ageSecondaryValue) {
        this.ageSecondaryValue = ageSecondaryValue;
    }

    public String getAgeSecondaryValue() {
        return ageSecondaryValue;
    }

    public void setPpc(String ppc) {
        this.ppc = ppc;
    }

    public String getPpc() {
        return ppc;
    }

    public void setVolumeAnnual(String volumeAnnual) {
        this.volumeAnnual = volumeAnnual;
    }

    public String getVolumeAnnual() {
        return volumeAnnual;
    }

    public void setVolumeMonthly(String volumeMonthly) {
        this.volumeMonthly = volumeMonthly;
    }

    public String getVolumeMonthly() {
        return volumeMonthly;
    }

    private static final long serialVersionUID = 1L;
    private String scoreDifficulty;
    private String scoreContent;
    private String scoreLinks;
    private String scoreFacebookLikes;
    private String scoreGenderMale;
    private String scoreGenderFemale;
    private String agePrimaryDescription;
    private String agePrimaryValue;
    private String ageSecondaryDescription;
    private String ageSecondaryValue;
    private String ppc;
    private String volumeAnnual;
    private String volumeMonthly;
}
