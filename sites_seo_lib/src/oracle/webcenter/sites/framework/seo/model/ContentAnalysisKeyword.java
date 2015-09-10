package oracle.webcenter.sites.framework.seo.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class ContentAnalysisKeyword implements Serializable {

    public ContentAnalysisKeyword() {
        kwc = null;
        kwcIndicator = null;
        kwd = null;
        kwf = null;
        kwlText = null;
        kwo = null;
        kwod = null;
        kwp = null;
        kwr = null;
        kws = null;
        kwl = null;
        text = null;
        kwe = new ArrayList();
    }

    public void setKwc(String kwc) {
        this.kwc = kwc;
        if (this.kwc != null) {
            double dbl = Double.parseDouble(kwc);
            if (dbl > 4D)
                setKwcIndicator("success");
            else if (dbl > 1.0D)
                setKwcIndicator("warning");
            else
                setKwcIndicator("important");
        }
    }

    public String getKwc() {
        return kwc;
    }

    public void setKwd(String kwd) {
        this.kwd = kwd;
    }

    public String getKwd() {
        return kwd;
    }

    public void setKwe(List kwe) {
        this.kwe = kwe;
    }

    public List getKwe() {
        return kwe;
    }

    public void setKwf(String kwf) {
        this.kwf = kwf;
    }

    public String getKwf() {
        return kwf;
    }

    public void setKwlText(String kwlText) {
        this.kwlText = kwlText;
    }

    public String getKwlText() {
        return kwlText;
    }

    public void setKwo(String kwo) {
        this.kwo = kwo;
    }

    public String getKwo() {
        return kwo;
    }

    public void setKwod(String kwod) {
        this.kwod = kwod;
    }

    public String getKwod() {
        return kwod;
    }

    public void setKwp(String kwp) {
        this.kwp = kwp;
    }

    public String getKwp() {
        return kwp;
    }

    public void setKwr(String kwr) {
        this.kwr = kwr;
    }

    public String getKwr() {
        return kwr;
    }

    public void setKws(String kws) {
        this.kws = kws;
    }

    public String getKws() {
        return kws;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setKwl(String kwl) {
        this.kwl = kwl;
    }

    public String getKwl() {
        return kwl;
    }

    public void setKwcIndicator(String kwcIndicator) {
        this.kwcIndicator = kwcIndicator;
    }

    public String getKwcIndicator() {
        return kwcIndicator;
    }

    private static final long serialVersionUID = 1L;
    private String kwc;
    private String kwcIndicator;
    private String kwd;
    private String kwf;
    private String kwlText;
    private String kwo;
    private String kwod;
    private String kwp;
    private String kwr;
    private String kws;
    private String kwl;
    private String text;
    private List kwe;
}
