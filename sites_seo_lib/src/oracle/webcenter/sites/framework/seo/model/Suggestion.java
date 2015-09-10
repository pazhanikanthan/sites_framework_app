package oracle.webcenter.sites.framework.seo.model;

import java.io.Serializable;

public class Suggestion implements Serializable {

    public Suggestion() {
        term = null;
        competition = null;
        popularity = null;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getCompetition() {
        return competition;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPopularity() {
        return popularity;
    }

    private static final long serialVersionUID = 1L;
    private String term;
    private String competition;
    private String popularity;
}
