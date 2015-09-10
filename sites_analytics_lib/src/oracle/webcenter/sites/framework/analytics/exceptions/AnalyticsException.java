package oracle.webcenter.sites.framework.analytics.exceptions;


public class AnalyticsException extends Exception {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    public AnalyticsException(Throwable throwable) {
        super(throwable);
    }

    public AnalyticsException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public AnalyticsException(String string) {
        super(string);
    }

    public AnalyticsException() {
    }

}
