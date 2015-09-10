package oracle.webcenter.sites.framework.analytics.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import oracle.webcenter.sites.framework.analytics.exceptions.AnalyticsException;
import oracle.webcenter.sites.framework.analytics.model.AnalyticsResult;
import oracle.webcenter.sites.framework.analytics.model.Asset;
import oracle.webcenter.sites.framework.analytics.model.ClickVsView;
import oracle.webcenter.sites.framework.analytics.model.FilterParams;
import oracle.webcenter.sites.framework.analytics.model.Payload;
import oracle.webcenter.sites.framework.analytics.model.Segment;
import oracle.webcenter.sites.framework.analytics.model.TrackEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DatabaseUtil implements Serializable {

    public DatabaseUtil() {
        dataSource = null;
        schemaFilePath = null;
        logger = LogFactory.getLog(getClass());
    }

    public void closeAll(Connection connection, Statement statement, ResultSet resultSet) {
        close(resultSet);
        close(statement);
        close(connection);
    }

    public void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException expSQL) {
            logger.warn("Cannot close ResultSet");
        } finally {
            resultSet = null;
        }
    }

    public void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
        } catch (SQLException expSQL) {
            logger.warn("Cannot close Statement");
        } finally {
            statement = null;
        }
    }

    public void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException expSQL) {
            logger.warn("Cannot close Connection");
        } finally {
            connection = null;
        }
    }

    public void initialise() throws AnalyticsException {
        logger.info("Entered");
        try {
            connection = getConnection();
            boolean isInitialised = isInitialised();
            logger.debug((new StringBuilder()).append("Database is already initialised? :").append(isInitialised).toString());
            if (!isInitialised)
                setupSchema();
        } catch (Exception expGeneral) {
            throw new AnalyticsException(expGeneral);
        }
        logger.info("Leaving");
    }

    private void setupSchema() throws AnalyticsException {
        logger.info("Entered");
        try {
            List stmts = loadStatements();
            connection.setAutoCommit(false);
            Statement statement = null;
            for (int i = 0; i < stmts.size(); i++) {
                String strStatement = (String) stmts.get(i);
                statement = connection.createStatement();
                if (strStatement.toUpperCase().startsWith("CREATE")) {
                    int result = statement.executeUpdate(strStatement);
                    logger.debug((new StringBuilder()).append("Statement Execute status :").append(result).toString());
                } else {
                    statement.executeQuery(strStatement);
                }
                close(statement);
            }

        } catch (Exception expGeneral) {
            throw new AnalyticsException(expGeneral);
        }
        logger.info("Leaving");
    }

    public void shutdown() throws AnalyticsException {
        logger.info("Entered");
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            statement.close();
            connection.close();
        } catch (Exception expGeneral) {
            throw new AnalyticsException(expGeneral);
        }
        logger.info("Leaving");
    }

    private List loadStatements() throws Exception {
        String databaseSQLFilePath = getSchemaFilePath();
        InputStream is = new FileInputStream(databaseSQLFilePath);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
        List stmts = new ArrayList();
        String strstmt = "";
        for (String ln = bReader.readLine(); ln != null;)
            if (ln.startsWith("REM") || ln.startsWith("COMMIT")) {
                ln = bReader.readLine();
            } else {
                strstmt = (new StringBuilder()).append(strstmt).append(ln).toString();
                if (strstmt.endsWith(";")) {
                    strstmt = strstmt.replaceAll(";", "");
                    logger.debug(strstmt);
                    stmts.add(strstmt);
                    strstmt = "";
                    ln = bReader.readLine();
                } else {
                    ln = bReader.readLine();
                }
            }

        return stmts;
    }

    private synchronized boolean isInitialised() throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        boolean result = false;
        try {
            statement = connection.createStatement();
            resultSet =
                statement.executeQuery("SELECT COUNT (1) AS CNT_TABLES FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'");
            if (resultSet.next()) {
                int count = resultSet.getInt("CNT_TABLES");
                if (count > 0) {
                    result = true;
                }
            }
            resultSet.close();
            statement.close();
            closeAll(null, statement, resultSet);
        } catch (SQLException expSQL) {
            expSQL.printStackTrace();
            throw expSQL;
        } finally {
            closeAll(null, statement, resultSet);
        }
        return result;
    }

    public synchronized void updateAssetDetail(Asset asset) throws AnalyticsException {
        boolean result = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug((new StringBuilder()).append("Asset:").append(asset).toString());
            }
            statement = connection.prepareStatement("SELECT COUNT (1) AS CNT_ASSET FROM ASSET where ASSET_ID = ?");
            statement.setString(1, asset.getId());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("CNT_ASSET");
                if (count > 0) {
                    result = true;
                }
            }
            closeAll(null, statement, resultSet);
            if (!result) {
                if (logger.isDebugEnabled()) {
                    logger.debug((new StringBuilder()).append("No Asset found:").append(asset).toString());
                }
                statement =
                    connection.prepareStatement("INSERT INTO ASSET (ASSET_ID, ASSET_TYPE, ASSET_LABEL) VALUES (?, ?, ?)");
                statement.setString(1, asset.getId());
                statement.setString(2, asset.getType());
                statement.setString(3, asset.getLabel());
                int insertCount = statement.executeUpdate();
                logger.debug((new StringBuilder()).append("No of Asset (Master) Rows inserted:").append(insertCount).toString());
                closeAll(null, statement, null);
            } else if (logger.isDebugEnabled()) {
                logger.debug((new StringBuilder()).append("Asset already found:").append(asset).toString());
            }
        } catch (Exception expGeneral) {
            throw new AnalyticsException(expGeneral);
        }
        closeAll(null, statement, null);
    }

    public synchronized AnalyticsResult getResults(TrackEvent event,
                                                   FilterParams filterParams) throws AnalyticsException {
        PreparedStatement statement;
        ResultSet resultSet;
        AnalyticsResult analyticsResult;
        statement = null;
        resultSet = null;
        analyticsResult = new AnalyticsResult();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug((new StringBuilder()).append("ID:").append(event.getId()).toString());
                if (filterParams.isSegmentFilterSelected()) {
                    String arr$[] = filterParams.getSegments();
                    int len$ = arr$.length;
                    for (int i$ = 0; i$ < len$; i$++) {
                        String filterSegment = arr$[i$];
                        logger.debug((new StringBuilder()).append("Selected Segment :").append(filterSegment).toString());
                    }

                }
                if (filterParams.isStartDateFilterSelected())
                    logger.debug((new StringBuilder()).append("Start Date :").append(filterParams.getStartDate()).toString());
                if (filterParams.isEndDateFilterSelected())
                    logger.debug((new StringBuilder()).append("End Date :").append(filterParams.getEndDate()).toString());
            }
            StringBuilder dateClauseFilter = new StringBuilder();
            SimpleDateFormat stringToDateFormat = new SimpleDateFormat("dd-mm-yyyy");
            SimpleDateFormat dateToTimestampFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            if (filterParams.isStartDateFilterSelected()) {
                java.util.Date date = stringToDateFormat.parse(filterParams.getStartDate());
                dateClauseFilter.append((new StringBuilder()).append("AND ANALYTICS_TS >= '").append(dateToTimestampFormat.format(date)).append("' ").toString());
            }
            if (filterParams.isEndDateFilterSelected()) {
                java.util.Date date = stringToDateFormat.parse(filterParams.getEndDate());
                dateClauseFilter.append((new StringBuilder()).append("AND ANALYTICS_TS <= '").append(dateToTimestampFormat.format(date)).append("' ").toString());
            }
            String filterClause = getFilterClause(filterParams, event);
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT DISTINCT EVENT_TYPE FROM ANALYTICS ");
            builder.append("WHERE EVENT_ID = ? ");
            builder.append(filterClause);
            if (logger.isDebugEnabled())
                logger.debug((new StringBuilder()).append("SQL Statement 1 :").append(builder.toString()).toString());
            statement = connection.prepareStatement(builder.toString());
            statement.setString(1, event.getId());
            resultSet = statement.executeQuery();
            boolean recordFound = false;
            if (resultSet.next()) {
                String type = resultSet.getString("EVENT_TYPE");
                event.setType(type);
                logger.debug((new StringBuilder()).append("Type:").append(type).toString());
                recordFound = true;
            }
            closeAll(null, statement, resultSet);
            analyticsResult.setRecordFound(recordFound);
            logger.debug((new StringBuilder()).append("recordFound:").append(recordFound).toString());
            if (recordFound) {
                builder = new StringBuilder();
                builder.append("SELECT COUNT (1) AS EVENT_VIEWED FROM ANALYTICS ");
                builder.append("WHERE EVENT_ID = ? ");
                builder.append("AND EVENT_TYPE = ? ");
                builder.append("AND ASSET_ID = ? ");
                builder.append("AND ASSET_TYPE = ? ");
                builder.append(filterClause);
                builder.append(dateClauseFilter.toString());
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("SQL Statement 2 :").append(builder.toString()).toString());
                statement = connection.prepareStatement(builder.toString());
                statement.setString(1, event.getId());
                statement.setString(2, event.getType());
                statement.setString(3, event.getId());
                statement.setString(4, event.getType());
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    long totalViewed = resultSet.getLong("EVENT_VIEWED");
                    if (logger.isDebugEnabled())
                        logger.debug((new StringBuilder()).append("totalViewed:").append(totalViewed).toString());
                    event.setTotalViewed(totalViewed);
                }
                closeAll(null, statement, resultSet);
                analyticsResult.setEvent(event);
                builder = new StringBuilder();
                builder.append("SELECT ASSET_ID, ASSET_TYPE, ANALYTICS_TYPE, ");
                builder.append("COUNT (1) AS ANALYTICS_COUNT ");
                builder.append("FROM ANALYTICS ");
                builder.append("WHERE EVENT_ID = ? ");
                builder.append("AND EVENT_TYPE = ? ");
                builder.append(filterClause);
                builder.append(dateClauseFilter.toString());
                builder.append("GROUP BY ASSET_ID, ASSET_TYPE, ANALYTICS_TYPE ");
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("SQL Statement 3 :").append(builder.toString()).toString());
                statement = connection.prepareStatement(builder.toString());
                statement.setString(1, event.getId());
                statement.setString(2, event.getType());
                resultSet = statement.executeQuery();
                Map values = new HashMap();
                do {
                    if (!resultSet.next())
                        break;
                    String assetId = resultSet.getString("ASSET_ID");
                    String assetType = resultSet.getString("ASSET_TYPE");
                    String analyticsType = resultSet.getString("ANALYTICS_TYPE");
                    String assetLabel = getAssetLabel(assetId, assetType);
                    long analyticsCount = resultSet.getLong("ANALYTICS_COUNT");
                    if (logger.isDebugEnabled()) {
                        logger.debug((new StringBuilder()).append("assetId:").append(assetId).toString());
                        logger.debug((new StringBuilder()).append("assetType:").append(assetType).toString());
                        logger.debug((new StringBuilder()).append("assetLabel:").append(assetLabel).toString());
                        logger.debug((new StringBuilder()).append("analyticsType:").append(analyticsType).toString());
                        logger.debug((new StringBuilder()).append("analyticsCount:").append(analyticsCount).toString());
                    }
                    if (!assetId.equalsIgnoreCase(event.getId())) {
                        Asset asset = null;
                        if (values.containsKey(assetId)) {
                            asset = (Asset) values.get(assetId);
                        } else {
                            asset = new Asset(assetType, assetId, assetLabel);
                            values.put(assetId, asset);
                        }
                        if ("Clicked".equals(analyticsType))
                            asset.setTotalClicked(analyticsCount);
                        else
                            asset.setTotalViewed(analyticsCount);
                    }
                } while (true);
                for (Iterator iter = values.keySet().iterator(); iter.hasNext();) {
                    String assetId = (String) iter.next();
                    Asset asset = (Asset) values.get(assetId);
                    if (asset.getType().equalsIgnoreCase("Segments")) {
                        Segment segment = new Segment(asset);
                        segment.setSelected(isCurrentSegment(segment, filterParams));
                        segment = loadSegmentAnalytics(segment);
                        analyticsResult.addSegment(segment);
                    } else {
                        event.addAsset(asset);
                    }
                }

                Collections.sort(event.getAssets(), new AssetComparator());
                builder = new StringBuilder();
                builder.append("SELECT ANALYTICS_TS, ASSET_TYPE, ANALYTICS_TYPE  ");
                builder.append("FROM ANALYTICS ");
                builder.append("WHERE EVENT_ID = ? ");
                builder.append("AND EVENT_TYPE = ? ");
                builder.append("AND ASSET_TYPE != 'Segments' ");
                builder.append(dateClauseFilter.toString());
                builder.append("ORDER BY ANALYTICS_TS ASC ");
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("ClickVsView SQL Statement :").append(builder.toString()).toString());
                statement = connection.prepareStatement(builder.toString());
                statement.setString(1, event.getId());
                statement.setString(2, event.getType());
                resultSet = statement.executeQuery();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Map valuesClickVsView = new LinkedHashMap();
                do {
                    if (!resultSet.next())
                        break;
                    java.util.Date date = new java.util.Date(resultSet.getTimestamp("ANALYTICS_TS").getTime());
                    String assetType = resultSet.getString("ASSET_TYPE");
                    String analyticsType = resultSet.getString("ANALYTICS_TYPE");
                    if (logger.isDebugEnabled()) {
                        logger.debug((new StringBuilder()).append("Result Set date          :").append(date).toString());
                        logger.debug((new StringBuilder()).append("Result Set assetType     :").append(assetType).toString());
                        logger.debug((new StringBuilder()).append("Result Set analyticsType :").append(analyticsType).toString());
                    }
                    if (!analyticsType.equalsIgnoreCase("Viewed") || assetType.equalsIgnoreCase(event.getType())) {
                        String dateAsString = dateFormat.format(date);
                        ClickVsView clickVsView = null;
                        if (valuesClickVsView.containsKey(dateAsString)) {
                            clickVsView = (ClickVsView) valuesClickVsView.get(dateAsString);
                        } else {
                            clickVsView = new ClickVsView();
                            clickVsView.setDate(date);
                            valuesClickVsView.put(dateAsString, clickVsView);
                        }
                        if (analyticsType.equalsIgnoreCase("Viewed"))
                            clickVsView.addViewCount();
                        if (analyticsType.equalsIgnoreCase("Clicked"))
                            clickVsView.addClickCount();
                    }
                } while (true);
                String dateString;
                for (Iterator iter = valuesClickVsView.keySet().iterator(); iter.hasNext();
                     analyticsResult.addClickVsView((ClickVsView) valuesClickVsView.get(dateString)))
                    dateString = (String) iter.next();

                builder = new StringBuilder();
                builder.append("SELECT ANALYTICS_TS, ASSET_ID ");
                builder.append("FROM ANALYTICS ");
                builder.append("WHERE EVENT_ID = ? ");
                builder.append("AND EVENT_TYPE = ? ");
                builder.append("AND ASSET_TYPE = 'Segments' ");
                builder.append("ORDER BY ANALYTICS_TS, ASSET_ID ASC ");
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("Segmentation Timeline SQL Statement :").append(builder.toString()).toString());
                statement = connection.prepareStatement(builder.toString());
                statement.setString(1, event.getId());
                statement.setString(2, event.getType());
                for (resultSet = statement.executeQuery(); resultSet.next();) {
                    java.util.Date date = new java.util.Date(resultSet.getTimestamp("ANALYTICS_TS").getTime());
                    String assetId = resultSet.getString("ASSET_ID");
                    if (logger.isDebugEnabled()) {
                        logger.debug((new StringBuilder()).append("Result Set date       :").append(date).toString());
                        logger.debug((new StringBuilder()).append("Result Set assetId    :").append(assetId).toString());
                    }
                    String dateAsString = dateFormat.format(date);
                    Iterator i$ = analyticsResult.getSegments().iterator();
                    while (i$.hasNext()) {
                        Segment segment = (Segment) i$.next();
                        segment.setTimeSeriesIndicator(dateAsString, assetId);
                    }
                }

                closeAll(null, statement, resultSet);
            }
        } catch (AnalyticsException expAnalytics) {
            throw expAnalytics;
        } catch (Exception expGeneral) {
            throw new AnalyticsException(expGeneral);
        }
        closeAll(null, statement, resultSet);
        return analyticsResult;
    }

    private boolean isCurrentSegment(Segment segment, FilterParams filterParams) {
        if (filterParams.isSegmentFilterSelected()) {
            String arr$[] = filterParams.getSegments();
            int len$ = arr$.length;
            for (int i$ = 0; i$ < len$; i$++) {
                String filterSegment = arr$[i$];
                if (filterSegment.equalsIgnoreCase(segment.getId()))
                    return true;
            }

            return false;
        } else {
            return true;
        }
    }

    private Segment loadSegmentAnalytics(Segment segment) throws AnalyticsException {
        PreparedStatement statement;
        ResultSet resultSet;
        StringBuilder builder;
        statement = null;
        resultSet = null;
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("Segment ID:").append(segment.getId()).toString());
        builder = new StringBuilder();
        builder.append("SELECT ASSET_ID, ASSET_TYPE, ANALYTICS_TYPE, COUNT (1) AS ANALYTICS_COUNT ");
        builder.append("FROM ANALYTICS ");
        builder.append("WHERE EVENT_ID IN ");
        builder.append("( ");
        builder.append("       SELECT EVENT_ID FROM ANALYTICS ");
        builder.append("       WHERE ASSET_TYPE = 'Segments' ");
        builder.append("       AND ASSET_ID = ? ");
        builder.append(") ");
        builder.append("AND ASSET_TYPE = 'AVIArticle' ");
        builder.append("GROUP BY ASSET_ID, ASSET_TYPE, ANALYTICS_TYPE ");
        if (logger.isDebugEnabled())
            logger.debug((new StringBuilder()).append("Segment Analytics SQL:").append(builder.toString()).toString());
        try {
            statement = connection.prepareStatement(builder.toString());
            statement.setString(1, segment.getId());
            resultSet = statement.executeQuery();
            Map values = new HashMap();
            while (resultSet.next()) {
                String assetId = resultSet.getString("ASSET_ID");
                String assetType = resultSet.getString("ASSET_TYPE");
                String analyticsType = resultSet.getString("ANALYTICS_TYPE");
                String assetLabel = getAssetLabel(assetId, assetType);
                long analyticsCount = resultSet.getLong("ANALYTICS_COUNT");
                if (logger.isDebugEnabled()) {
                    logger.debug((new StringBuilder()).append("assetId:").append(assetId).toString());
                    logger.debug((new StringBuilder()).append("assetType:").append(assetType).toString());
                    logger.debug((new StringBuilder()).append("assetLabel:").append(assetLabel).toString());
                    logger.debug((new StringBuilder()).append("analyticsType:").append(analyticsType).toString());
                    logger.debug((new StringBuilder()).append("analyticsCount:").append(analyticsCount).toString());
                }
                Asset asset = null;
                if (values.containsKey(assetId)) {
                    asset = (Asset) values.get(assetId);
                } else {
                    asset = new Asset(assetType, assetId, assetLabel);
                    values.put(assetId, asset);
                }
                if ("Clicked".equals(analyticsType))
                    asset.setTotalClicked(analyticsCount);
                else
                    asset.setTotalViewed(analyticsCount);
            }
            Asset asset;
            for (Iterator iter = values.keySet().iterator(); iter.hasNext(); segment.addAsset(asset)) {
                String assetId = (String) iter.next();
                asset = (Asset) values.get(assetId);
            }

            Collections.sort(segment.getAssets(), new AssetComparator());
        } catch (Exception expGeneral) {
            expGeneral.printStackTrace();
            throw new AnalyticsException(expGeneral);
        }
        closeAll(null, statement, resultSet);
        return segment;
    }

    private synchronized String getAssetLabel(String assetId, String assetType) throws AnalyticsException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String assetLabel = null;
        try {
            statement = null;
            resultSet = null;
            assetLabel = null;
            try {
                StringBuilder builder = new StringBuilder();
                builder.append("SELECT ASSET_LABEL ");
                builder.append("FROM ASSET ");
                builder.append("WHERE ASSET_ID = ? ");
                builder.append("AND ASSET_TYPE = ? ");
                statement = connection.prepareStatement(builder.toString());
                statement.setString(1, assetId);
                statement.setString(2, assetType);
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    assetLabel = resultSet.getString("ASSET_LABEL");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug((new StringBuilder()).append(assetType).append(":").append(assetId).append(":").append(assetLabel).toString());
                }
                closeAll(null, statement, resultSet);
            } catch (Exception expGeneral) {
                throw new AnalyticsException(expGeneral);
            }
            closeAll(null, statement, resultSet);
        } catch (Exception exception) {
            throw new AnalyticsException(exception);
        }
        return assetLabel;
    }

    public synchronized void createAnalyticData(Payload payload) throws AnalyticsException {
        PreparedStatement statement = null;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO ANALYTICS ");
            builder.append("(ANALYTICS_ID, PAYLOAD_ID, EVENT_ID, EVENT_TYPE, ASSET_ID, ASSET_TYPE, ANALYTICS_TYPE, ANALYTICS_TS) ");
            builder.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            connection.setAutoCommit(false);
            for (Iterator iterator = payload.getTrackEvents().values().iterator(); iterator.hasNext();) {
                TrackEvent trackEvent = (TrackEvent) iterator.next();
                insertAnalyticData(connection, builder.toString(), payload.getId(), trackEvent.getId(),
                                   trackEvent.getType(), trackEvent.getId(), trackEvent.getType(), payload.getType(),
                                   payload.getTimestamp());
                Asset asset;
                for (Iterator i$ = trackEvent.getAssets().iterator(); i$.hasNext();
                     insertAnalyticData(connection, builder.toString(), payload.getId(), trackEvent.getId(),
                                        trackEvent.getType(), asset.getId(), asset.getType(), payload.getType(),
                                        payload.getTimestamp()))
                    asset = (Asset) i$.next();

                Iterator iteratorSegment = payload.getSegments().values().iterator();
                while (iteratorSegment.hasNext()) {
                    Asset segment = (Asset) iteratorSegment.next();
                    insertAnalyticData(connection, builder.toString(), payload.getId(), trackEvent.getId(),
                                       trackEvent.getType(), segment.getId(), segment.getType(), payload.getType(),
                                       payload.getTimestamp());
                }
            }

            connection.setAutoCommit(true);
            connection.commit();
            closeAll(null, statement, null);
        } catch (Exception expGeneral) {
            throw new AnalyticsException(expGeneral);
        }
        closeAll(null, statement, null);
    }

    public TrackEvent populateTrackEvent(String payloadId, String eventId, String assetId) throws AnalyticsException {
        PreparedStatement statement = null;
        TrackEvent trackEvent = null;
        ResultSet resultSet = null;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT EVENT_TYPE, ASSET_TYPE FROM ANALYTICS ");
            builder.append("WHERE PAYLOAD_ID = ? ");
            builder.append("AND EVENT_ID = ? ");
            builder.append("AND ASSET_ID = ? ");
            statement = connection.prepareStatement(builder.toString());
            statement.setString(1, payloadId);
            statement.setString(2, eventId);
            statement.setString(3, assetId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                trackEvent = new TrackEvent(resultSet.getString("EVENT_TYPE"), eventId, null);
                Asset asset = new Asset(resultSet.getString("ASSET_TYPE"), assetId, null);
                trackEvent.addAsset(asset);
            }
            closeAll(null, statement, resultSet);
        } catch (Exception expGeneral) {
            throw new AnalyticsException(expGeneral);
        }
        return trackEvent;
    }

    public synchronized void deleteAsset() throws AnalyticsException {
        PreparedStatement statement = null;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("DELETE FROM ASSET ");
            statement = connection.prepareStatement(builder.toString());
            int delCount = statement.executeUpdate();
            logger.debug((new StringBuilder()).append("delCount : ").append(delCount).toString());
            closeAll(null, statement, null);
            connection.commit();
            closeAll(null, statement, null);
        } catch (Exception expGeneral) {
            throw new AnalyticsException(expGeneral);
        }
    }

    private void insertAnalyticData(Connection connection, String insertSql, String payloadId, String eventId,
                                    String eventType, String assetId, String assetType, String analyticsType,
                                    long timestamp) throws SQLException {
        PreparedStatement statement = null;
        statement = connection.prepareStatement(insertSql);
        statement.setLong(1, getSequenceNextVal("ANALYTICS_SEQ"));
        statement.setString(2, payloadId);
        statement.setString(3, eventId);
        statement.setString(4, eventType);
        statement.setString(5, assetId);
        statement.setString(6, assetType);
        statement.setString(7, analyticsType);
        statement.setTimestamp(8, new Timestamp(timestamp));
        statement.executeUpdate();
        logger.debug((new StringBuilder()).append("Row [").append(assetType).append(":").append(assetId).append("] inserted").toString());
        statement.close();
    }

    private synchronized long getSequenceNextVal(String sequenceName) {
        Statement statement = null;
        ResultSet resultSet = null;
        long result = 1L;
        try {
            statement = connection.createStatement();
            resultSet =
                statement.executeQuery((new StringBuilder()).append("call next value for ").append(sequenceName).toString());
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            closeAll(null, statement, resultSet);
        } catch (Exception expGeneral) {
            new AnalyticsException(expGeneral);
        }
        return result;
    }

    private Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private String getFilterClause(FilterParams filterParams, TrackEvent event) throws Exception {
        StringBuilder filterClause;
        PreparedStatement statement;
        ResultSet resultSet;
        filterClause = new StringBuilder();
        statement = null;
        resultSet = null;
        try {
            StringBuilder clauseBuilder = new StringBuilder();
            if (filterParams.isSegmentFilterSelected()) {
                StringBuilder builderSegment = new StringBuilder();
                String arr$[] = filterParams.getSegments();
                int len$ = arr$.length;
                for (int i$ = 0; i$ < len$; i$++) {
                    String filterSegment = arr$[i$];
                    builderSegment.append((new StringBuilder()).append("'").append(filterSegment).append("', ").toString());
                }

                String segmentClause = builderSegment.toString();
                if (segmentClause != null && segmentClause.length() > 0) {
                    if (segmentClause.endsWith(", "))
                        segmentClause = segmentClause.substring(0, segmentClause.length() - 2);
                    clauseBuilder.append((new StringBuilder()).append("AND ASSET_ID IN (").append(segmentClause).append(") ").toString());
                }
            }
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT DISTINCT PAYLOAD_ID ");
            builder.append("FROM ANALYTICS ");
            builder.append("WHERE EVENT_ID = ? ");
            builder.append(clauseBuilder.toString());
            boolean recordFound = false;
            statement = connection.prepareStatement(builder.toString());
            statement.setString(1, event.getId());
            resultSet = statement.executeQuery();
            int totalCount;
            for (totalCount = 0; resultSet.next(); totalCount++) {
                String payloadId = resultSet.getString("PAYLOAD_ID");
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("payloadId:").append(payloadId).toString());
                filterClause.append((new StringBuilder()).append("'").append(payloadId).append("', ").toString());
                recordFound = true;
            }

            if (logger.isDebugEnabled()) {
                logger.debug((new StringBuilder()).append("Record Found :").append(recordFound).toString());
                logger.debug((new StringBuilder()).append("Total Payload Count:").append(totalCount).toString());
            }
            closeAll(null, statement, resultSet);
            if (recordFound) {
                String strFilter = filterClause.toString();
                if (strFilter.endsWith(", ")) {
                    strFilter = strFilter.substring(0, strFilter.length() - 2);
                    filterClause = new StringBuilder();
                    filterClause.append((new StringBuilder()).append("AND PAYLOAD_ID IN ( ").append(strFilter).append(" ) ").toString());
                }
            }
        } catch (Exception expGeneral) {
            throw expGeneral;
        }
        return filterClause.toString();
    }

    public void setSchemaFilePath(String schemaFilePath) {
        this.schemaFilePath = schemaFilePath;
    }

    public String getSchemaFilePath() {
        return schemaFilePath;
    }

    private static final long serialVersionUID = 1L;
    private transient DataSource dataSource;
    private String schemaFilePath;
    private transient Log logger;
    private static Connection connection = null;

}
