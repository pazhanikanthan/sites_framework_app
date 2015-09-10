package oracle.webcenter.sites.framework.analytics.services.impl.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.model.Account;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.Profile;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import com.google.api.services.analytics.model.Webproperty;

import java.io.IOException;
import java.io.Serializable;

import java.util.Iterator;
import java.util.List;

import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;
import oracle.webcenter.sites.framework.analytics.model.google.AnalyticsData;
import oracle.webcenter.sites.framework.analytics.model.google.AnalyticsQuery;
import oracle.webcenter.sites.framework.analytics.model.google.AnalyticsResults;
import oracle.webcenter.sites.framework.analytics.model.google.ServiceAccountPrivateKey;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GoogleAnalyticsServiceImpl implements Serializable {

    public GoogleAnalyticsServiceImpl() {
        logger = LogFactory.getLog(getClass());
        analytics = null;
        accountName = null;
        profileId = null;
        enabled = false;
        serviceAccountEmail = null;
        serviceAccountPrivateKey = null;
    }

    public void initialise() throws AnalyticsException {
        logger.info("Entered");
        try {
            JSON_FACTORY = new JacksonFactory();
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            analytics = initializeAnalytics();
            profileId = findProfileId(analytics, getAccountName());
            logger.info((new StringBuilder()).append("Profile ID: ").append(profileId).toString());
            if (profileId == null)
                logger.error("No profiles found.");
            enabled = true;
        } catch (Exception expGeneral) {
            logger.error("Exception during initalising.", expGeneral);
            expGeneral.printStackTrace();
            enabled = false;
        }
        logger.info("Leaving");
    }

    public AnalyticsResults getResults(AnalyticsQuery analyticsQuery) throws AnalyticsException {
        logger.info("Entered");
        AnalyticsResults results = new AnalyticsResults();
        try {
            String tableId = (new StringBuilder()).append("ga:").append(profileId).toString();
            com.google.api.services.analytics.Analytics.Data.Ga.Get get = analytics.data().ga().get(tableId, analyticsQuery.getStartDate(), analyticsQuery.getEndDate(), analyticsQuery.getMetrics());
            if (!StringUtils.isBlank(analyticsQuery.getDimensions())) {
                logger.info((new StringBuilder()).append("Adding Dimensions:").append(analyticsQuery.getDimensions()).toString());
                get.setDimensions(analyticsQuery.getDimensions());
            }
            if (!StringUtils.isBlank(analyticsQuery.getSort())) {
                logger.info((new StringBuilder()).append("Adding Sort:").append(analyticsQuery.getSort()).toString());
                get.setSort(analyticsQuery.getSort());
            }
            if (!StringUtils.isBlank(analyticsQuery.getFilters())) {
                logger.info((new StringBuilder()).append("Adding Filter:").append(analyticsQuery.getFilters()).toString());
                get.setFilters(analyticsQuery.getFilters());
            }
            if (analyticsQuery.getMaxResults().intValue() > 0)
                get.setMaxResults(analyticsQuery.getMaxResults());
            else {
                get.setMaxResults(Integer.valueOf(50));
            }
            GaData gaData = get.execute();
            if (gaData.getRows() == null || gaData.getRows().isEmpty()) {
                logger.info("No results Found.");
            } else {
                results.setEmpty(false);
                String header;
                for (Iterator i$ = gaData.getColumnHeaders().iterator(); i$.hasNext(); results.addHeader(header)) {
                    com.google.api.services.analytics.model.GaData.ColumnHeaders columnHeader =
                        (com.google.api.services.analytics.model.GaData.ColumnHeaders) i$.next();
                    header = columnHeader.getName();
                    logger.info((new StringBuilder()).append("Header : ").append(header).toString());
                }

                AnalyticsData analyticsData;
                for (Iterator i$ = gaData.getRows().iterator(); i$.hasNext(); results.addData(analyticsData)) {
                    List row = (List) i$.next();
                    analyticsData = new AnalyticsData();
                    for (int i = 0; i < row.size(); i++) {
                        String data = (String) row.get(i);
                        header = (String) results.getHeaders().get(i);
                        logger.info((new StringBuilder()).append("Header : ").append(header).toString());
                        logger.info((new StringBuilder()).append("Data   : ").append(data).toString());
                        analyticsData.put(header, data);
                    }

                }

            }
        } catch (Exception expGeneral) {
            throw new AnalyticsException(expGeneral);
        }
        logger.info("Leaving");
        return results;
    }

    public void setAccountName(String profileName) {
        accountName = profileName;
    }

    public String getAccountName() {
        return accountName;
    }

    private Analytics initializeAnalytics() throws Exception {
        Credential credential = authorize();
        return (new com.google.api.services.analytics.Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                                                                        credential)).setApplicationName("").build();
    }

    @SuppressWarnings({ "oracle.jdeveloper.java.semantic-warning", "deprecation" })
    private Credential authorize() throws Exception {
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        com.google.api.client.googleapis.auth.oauth2.GoogleCredential.Builder builder =
            new com.google.api.client.googleapis.auth.oauth2.GoogleCredential.Builder();
        builder.setTransport(HTTP_TRANSPORT);
        builder.setJsonFactory(JSON_FACTORY);
        builder.setServiceAccountId(getServiceAccountEmail());
        builder.setServiceAccountScopes(new String[] { "https://www.googleapis.com/auth/analytics" });
        builder.setServiceAccountPrivateKeyFromP12File(getServiceAccountPrivateKey().getFile());
        GoogleCredential credential = builder.build();
        return credential;
    }

    private String findProfileId(Analytics analytics, String accountName) throws IOException {
        logger.info((new StringBuilder()).append("Account Name: ").append(accountName).toString());
        String profileId = null;
        Accounts accounts = (Accounts) analytics.management().accounts().list().execute();
        if (accounts.getItems().isEmpty()) {
            logger.debug("No accounts found");
        } else {
            logger.debug((new StringBuilder()).append("Accounts found: ").append(accounts.getItems().size()).toString());
            String accountId = null;
            Iterator i$ = accounts.getItems().iterator();
            do {
                if (!i$.hasNext())
                    break;
                Account account = (Account) i$.next();
                String googleAccountName = account.getName();
                logger.debug((new StringBuilder()).append("googleAccountName: ").append(googleAccountName).toString());
                logger.debug((new StringBuilder()).append("accountName: ").append(accountName).toString());
                if (!googleAccountName.equalsIgnoreCase(accountName))
                    continue;
                accountId = account.getId();
                break;
            } while (true);
            logger.debug((new StringBuilder()).append("accountId: ").append(accountId).toString());
            Webproperties webproperties =
                (Webproperties) analytics.management().webproperties().list(accountId).execute();
            if (webproperties.getItems().isEmpty()) {
                logger.debug("No Webproperties found");
            } else {
                String firstWebpropertyId = ((Webproperty) webproperties.getItems().get(0)).getId();
                Profiles profiles =
                    (Profiles) analytics.management().profiles().list(accountId, firstWebpropertyId).execute();
                if (profiles.getItems().isEmpty())
                    logger.debug("No profiles found");
                else
                    profileId = ((Profile) profiles.getItems().get(0)).getId();
            }
        }
        return profileId;
    }

    public Analytics getAnalytics() {
        return analytics;
    }

    public void setServiceAccountPrivateKey(ServiceAccountPrivateKey serviceAccountPrivateKey) {
        this.serviceAccountPrivateKey = serviceAccountPrivateKey;
    }

    public ServiceAccountPrivateKey getServiceAccountPrivateKey() {
        return serviceAccountPrivateKey;
    }

    public void setServiceAccountEmail(String serviceAccountEmail) {
        this.serviceAccountEmail = serviceAccountEmail;
    }

    public String getServiceAccountEmail() {
        return serviceAccountEmail;
    }

    public boolean isEnabled() {
        return enabled;
    }

    private static final long serialVersionUID = 1L;
    private transient Log logger;
    private static final String APPLICATION_NAME = "";
    private static HttpTransport HTTP_TRANSPORT;
    private static JsonFactory JSON_FACTORY = null;
    private transient Analytics analytics;
    private String accountName;
    private String profileId;
    private boolean enabled;
    private String serviceAccountEmail;
    private transient ServiceAccountPrivateKey serviceAccountPrivateKey;

}
