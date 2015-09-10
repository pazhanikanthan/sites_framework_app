package oracle.webcenter.sites.framework.seo.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import oracle.webcenter.sites.framework.seo.model.ContentAnalysis;
import oracle.webcenter.sites.framework.seo.model.ContentAnalysisKeyword;
import oracle.webcenter.sites.framework.seo.model.KeywordAnalysis;
import oracle.webcenter.sites.framework.seo.model.Suggestion;
import oracle.webcenter.sites.framework.seo.services.SEOService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScribeSEOServiceImpl implements SEOService {

    public ScribeSEOServiceImpl() {
        contentAnalysisServiceUrl = null;
        keywordAnalysisServiceUrl = null;
        suggestionsServiceUrl = null;
        connectTimeoutInMilliSeconds = 5000;
        logger = LogFactory.getLog(getClass());
    }

    public KeywordAnalysis getKeywordAnalysis(String keyword, String domain) {
        if (logger.isDebugEnabled()) {
            logger.debug((new StringBuilder()).append("keyword: ").append(keyword).toString());
            logger.debug((new StringBuilder()).append("domain: ").append(domain).toString());
        }
        KeywordAnalysis keywordAnalysis = new KeywordAnalysis();
        try {
            if (!StringUtils.isBlank(keyword)) {
                String keywordAnalysisJson = getSEOKeywordAnalysis(keyword, domain);
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("keywordAnalysisJson: ").append(keywordAnalysisJson).toString());
                JSONObject jsonObject = new JSONObject(keywordAnalysisJson);
                keywordAnalysis.setScoreDifficulty(String.valueOf(jsonObject.get("scoreDifficulty")));
                keywordAnalysis.setScoreContent(String.valueOf(jsonObject.get("scoreContent")));
                keywordAnalysis.setScoreLinks(String.valueOf(jsonObject.get("scoreLinks")));
                keywordAnalysis.setScoreFacebookLikes(String.valueOf(jsonObject.get("scoreFacebookLikes")));
                keywordAnalysis.setScoreGenderMale(String.valueOf(jsonObject.get("scoreGenderMale")));
                keywordAnalysis.setScoreGenderFemale(String.valueOf(jsonObject.get("scoreGenderFemale")));
                keywordAnalysis.setAgePrimaryDescription(String.valueOf(jsonObject.get("agePrimaryDescription")));
                keywordAnalysis.setAgePrimaryValue(String.valueOf(jsonObject.get("agePrimaryValue")));
                keywordAnalysis.setAgeSecondaryDescription(String.valueOf(jsonObject.get("ageSecondaryDescription")));
                keywordAnalysis.setAgeSecondaryValue(String.valueOf(jsonObject.get("ageSecondaryValue")));
                keywordAnalysis.setPpc(String.valueOf(jsonObject.get("ppc")));
                keywordAnalysis.setVolumeAnnual(String.valueOf(jsonObject.get("volumeAnnual")));
                keywordAnalysis.setVolumeMonthly(String.valueOf(jsonObject.get("volumeMonthly")));
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("keywordAnalysis: ").append(keywordAnalysis).toString());
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return keywordAnalysis;
    }

    public List getSuggestions(String keyword) {
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("keyword: ").append(keyword).toString());
        List suggestionList = new ArrayList();
        try {
            if (!StringUtils.isBlank(keyword)) {
                String suggestionsJson = getSEOSuggestions(keyword);
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("suggestionsJson: ").append(suggestionsJson).toString());
                JSONObject jsonObject = new JSONObject(suggestionsJson);
                JSONArray items = (JSONArray) jsonObject.get("suggestions");
                if (items != null && items.length() > 0) {
                    for (int index = 0; index < items.length(); index++) {
                        Suggestion suggestion = new Suggestion();
                        JSONObject item = items.getJSONObject(index);
                        suggestion.setTerm((String) item.get("term"));
                        suggestion.setCompetition(String.valueOf(item.get("competition")));
                        suggestion.setPopularity(String.valueOf(item.get("popularity")));
                        suggestionList.add(suggestion);
                    }

                }
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("suggestionList: ").append(suggestionList).toString());
            }
        } catch (JSONException jsonException) {
            logger.warn(jsonException);
        }
        return suggestionList;
    }

    public ContentAnalysis getContentAnalysis(String htmlTitle, String htmlDescription, String htmlHeadline,
                                              String htmlBody, String domain) {
        if (logger.isDebugEnabled()) {
            logger.debug((new StringBuilder()).append("htmlTitle: ").append(htmlTitle).toString());
            logger.debug((new StringBuilder()).append("htmlDescription: ").append(htmlDescription).toString());
            logger.debug((new StringBuilder()).append("htmlHeadline: ").append(htmlHeadline).toString());
            logger.debug((new StringBuilder()).append("htmlBody: ").append(htmlBody).toString());
            logger.debug((new StringBuilder()).append("domain: ").append(domain).toString());
        }
        ContentAnalysis contentAnalysis = new ContentAnalysis();
        try {
            String contentAnalysisJson =
                getSEOContentAnalysis(htmlTitle, htmlDescription, htmlHeadline, htmlBody, domain);
            System.out.println((new StringBuilder()).append("contentAnalysisJson:").append(contentAnalysisJson).toString());
            if (logger.isDebugEnabled())
                logger.debug((new StringBuilder()).append("contentAnalysisJson: ").append(contentAnalysisJson).toString());
            JSONObject jsonObject = new JSONObject(contentAnalysisJson);
            contentAnalysis.setDocScore(String.valueOf(jsonObject.get("docScore")));
            contentAnalysis.setFleschScore(String.valueOf(jsonObject.get("fleschScore")));
            contentAnalysis.setScribeScore(String.valueOf(jsonObject.get("scribeScore")));
            contentAnalysis.setDocScoresE(extractArray(jsonObject, "docScoreE"));
            JSONArray keywords = (JSONArray) jsonObject.get("keywords");
            if (keywords != null && keywords.length() > 0) {
                for (int index = 0; index < keywords.length(); index++) {
                    ContentAnalysisKeyword contentAnalysisKeyword = new ContentAnalysisKeyword();
                    JSONObject item = keywords.getJSONObject(index);
                    contentAnalysisKeyword.setKwc(String.valueOf(item.get("kwc")));
                    contentAnalysisKeyword.setKwd(String.valueOf(item.get("kwd")));
                    contentAnalysisKeyword.setKwe(extractArray(item, "kwe"));
                    contentAnalysisKeyword.setKwf(String.valueOf(item.get("kwf")));
                    contentAnalysisKeyword.setKwlText(String.valueOf(item.get("kwlText")));
                    contentAnalysisKeyword.setKwo((String) item.get("kwo"));
                    contentAnalysisKeyword.setKwod((String) item.get("kwod"));
                    contentAnalysisKeyword.setKwp((String) item.get("kwp"));
                    contentAnalysisKeyword.setKwl(String.valueOf(item.get("kwl")));
                    contentAnalysisKeyword.setText((String) item.get("text"));
                    contentAnalysisKeyword.setKwr(String.valueOf(item.get("kwr")));
                    contentAnalysisKeyword.setKws(String.valueOf(item.get("kws")));
                    contentAnalysis.addKeyword(contentAnalysisKeyword);
                }

            }
            contentAnalysis.setTags(extractArray(jsonObject, "tags"));
        } catch (JSONException jsonException) {
            logger.warn(jsonException);
        }
        return contentAnalysis;
    }

    private List extractArray(JSONObject jsonObject, String itemName) throws JSONException {
        List array = new ArrayList();
        JSONArray items = (JSONArray) jsonObject.get(itemName);
        if (items != null && items.length() > 0) {
            for (int index = 0; index < items.length(); index++)
                array.add((String) items.get(index));

        }
        return array;
    }

    public static void main(String a[]) {
        SEOService service = new ScribeSEOServiceImpl();
        service.getContentAnalysis(null, null, null, null, null);
        service.getKeywordAnalysis(null, null);
    }

    private String getSEOKeywordAnalysis(String keyword, String domain) {
        String data =
            (new StringBuilder()).append("{\"query\": \"").append(keyword).append("\", \"domain\": \"").append(domain).append("\"}").toString();
        return getResponse(getKeywordAnalysisServiceUrl(), data);
    }

    private String getSEOSuggestions(String keyword) {
        String data = (new StringBuilder()).append("{\"query\": \"").append(keyword).append("\" }").toString();
        return getResponse(getSuggestionsServiceUrl(), data);
    }

    private String getSEOContentAnalysis(String htmlTitle, String htmlDescription, String htmlHeadline, String htmlBody,
                                         String domain) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append((new StringBuilder()).append("\"htmlTitle\": \"").append(htmlTitle).append("\",").toString());
        builder.append((new StringBuilder()).append("\"htmlDescription\": \"").append(htmlDescription).append("\",").toString());
        builder.append((new StringBuilder()).append("\"htmlHeadline\": \"").append(htmlHeadline).append("\",").toString());
        builder.append((new StringBuilder()).append("\"htmlBody\": \"").append(htmlBody).append("\",").toString());
        builder.append((new StringBuilder()).append("\"domain\": \"").append(domain).append("\"").toString());
        builder.append("}");
        String data = builder.toString();
        return getResponse(getContentAnalysisServiceUrl(), data);
    }

    private String getResponse(String URL, String data) {
        String response = "";
        try {
            if (logger.isDebugEnabled()) {
                logger.debug((new StringBuilder()).append("URL: ").append(URL).toString());
                logger.debug((new StringBuilder()).append("data: ").append(data).toString());
            }
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(getConnectTimeoutInMilliSeconds());
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            if (conn.getResponseCode() != 200)
                throw new RuntimeException((new StringBuilder()).append("Failed : HTTP error code : ").append(conn.getResponseCode()).toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            if (logger.isDebugEnabled())
                logger.debug("Output from Server .... \n");
            StringBuilder builder = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null)
                builder.append(output);
            conn.disconnect();
            response = builder.toString();
        } catch (Exception expGeneral) {
            expGeneral.printStackTrace();
            logger.warn(expGeneral);
        }
        return response;
    }

    public void setContentAnalysisServiceUrl(String contentAnalysisServiceUrl) {
        this.contentAnalysisServiceUrl = contentAnalysisServiceUrl;
    }

    public String getContentAnalysisServiceUrl() {
        return contentAnalysisServiceUrl;
    }

    public void setKeywordAnalysisServiceUrl(String keywordAnalysisServiceUrl) {
        this.keywordAnalysisServiceUrl = keywordAnalysisServiceUrl;
    }

    public String getKeywordAnalysisServiceUrl() {
        return keywordAnalysisServiceUrl;
    }

    public void setSuggestionsServiceUrl(String suggestionsServiceUrl) {
        this.suggestionsServiceUrl = suggestionsServiceUrl;
    }

    public String getSuggestionsServiceUrl() {
        return suggestionsServiceUrl;
    }

    public void setConnectTimeoutInMilliSeconds(int connectTimeoutInMilliSeconds) {
        this.connectTimeoutInMilliSeconds = connectTimeoutInMilliSeconds;
    }

    public int getConnectTimeoutInMilliSeconds() {
        return connectTimeoutInMilliSeconds;
    }

    private static final long serialVersionUID = 1L;
    private String contentAnalysisServiceUrl;
    private String keywordAnalysisServiceUrl;
    private String suggestionsServiceUrl;
    private int connectTimeoutInMilliSeconds;
    private transient Log logger;
}
