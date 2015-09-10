package oracle.webcenter.sites.framework.services;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import oracle.webcenter.sites.framework.exceptions.APIException;
import oracle.webcenter.sites.framework.mappers.AuditRecordMapper;
import oracle.webcenter.sites.framework.model.AuditRecord;
import oracle.webcenter.sites.framework.model.AuditReport;
import oracle.webcenter.sites.framework.model.Criteria;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.jdbc.core.JdbcTemplate;

public class AuditService implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    private transient DataSource dataSource = null;
    private transient JdbcTemplate jdbcTemplateObject = null;

    public void create (AuditRecord auditRecord) throws APIException {
        logger.info("Entered");
        try {
            logger.debug("auditRecord:" + auditRecord);
            StringBuilder builder = new StringBuilder ();
            builder.append ("INSERT INTO audit_record (");
            builder.append ("                               audit_id, asset_id, asset_type, asset_name, ");
            builder.append ("                               target_asset_id, target_asset_type, target_asset_name, ");
            builder.append ("                               user_id, operation, audit_timestamp) ");
            builder.append ("VALUES (audit_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, SYSTIMESTAMP)");
            logger.debug("SQL:" + builder.toString ());
            Long targetId = null;
            String targetType = null;
            String targetName = null;
            if (auditRecord.getTargetAsset() != null) {
                targetId = auditRecord.getTargetAsset().getId();
                targetType = auditRecord.getTargetAsset().getType();
                targetName = auditRecord.getTargetAsset().getName();
            }
            getJdbcTemplateObject().update( builder.toString (), auditRecord.getSourceAsset().getId(), 
                                                            auditRecord.getSourceAsset().getType(), 
                                                            auditRecord.getSourceAsset().getName(), 
                                                            targetId, 
                                                            targetType,
                                                            targetName,
                                                            auditRecord.getUserId(), 
                                                            auditRecord.getOperation());
        }
        catch (Exception expGeneral) {
            throw new APIException ("Exception creating audit record " + auditRecord, expGeneral);
        }
        logger.info("Leaving");
    }
    
    public int count (Criteria criteria) throws APIException {
        logger.info("Entered");
        int totalRecordCount = 0;
        try {
            logger.debug("assetId:" + criteria.getAssetId());
            List <Object> params = new ArrayList <Object> ();
            StringBuilder builder = new StringBuilder ();
            StringBuilder clauseBuilder = new StringBuilder ();
            builder.append ("SELECT COUNT (1) AS CNT_RECORDS FROM audit_record ");
            buildQueryCriteria(criteria, params, clauseBuilder);
            builder.append (clauseBuilder.toString());
            totalRecordCount = getJdbcTemplateObject().queryForObject(builder.toString(), params.toArray(), Integer.class);
            logger.debug("totalRecordCount:" + totalRecordCount);
        }
        catch (Exception expGeneral) {
            throw new APIException ("Exception getting audit records count for assetId : " + criteria.getAssetId(), expGeneral);
        }
        logger.info("Leaving");
        return totalRecordCount;
    }

    private void buildQueryCriteria(Criteria criteria, List<Object> params, StringBuilder clauseBuilder) {
        if (criteria.isAssetSearch()) {
            clauseBuilder.append ("WHERE asset_id = ? ");
            params.add(criteria.getAssetId());
        }
        else {
            boolean criteriaAdded  = false;
            if (!isBlank(criteria.getName())) {
                clauseBuilder.append ("WHERE UPPER (ASSET_NAME) LIKE UPPER (?) ");
                criteriaAdded  = true;
                params.add("%" + criteria.getName() + "%");
            }
            if (!isBlank(criteria.getDestination())) {
                if (criteriaAdded) {
                    clauseBuilder.append ("AND ");
                }
                else {
                    clauseBuilder.append ("WHERE ");
                }
                clauseBuilder.append ("TARGET_ASSET_NAME = ? ");
                criteriaAdded  = true;
                params.add(criteria.getDestination());
            }
            if (!isBlank(criteria.getType())) {
                if (criteriaAdded) {
                    clauseBuilder.append ("AND ");
                }
                else {
                    clauseBuilder.append ("WHERE ");
                }
                clauseBuilder.append ("ASSET_TYPE = ? ");
                criteriaAdded  = true;
                params.add(criteria.getType());
            }
            if (!isBlank(criteria.getStartDate())) {
                if (criteriaAdded) {
                    clauseBuilder.append ("AND ");
                }
                else {
                    clauseBuilder.append ("WHERE ");
                }
                clauseBuilder.append ("AUDIT_TIMESTAMP >= TO_TIMESTAMP (?, 'DD-MM-YYYY') ");
                criteriaAdded  = true;
                params.add(criteria.getStartDate());
            }
            if (!isBlank(criteria.getEndDate())) {
                if (criteriaAdded) {
                    clauseBuilder.append ("AND ");
                }
                else {
                    clauseBuilder.append ("WHERE ");
                }
                clauseBuilder.append ("AUDIT_TIMESTAMP <= TO_TIMESTAMP (?, 'DD-MM-YYYY') + 1 ");
                criteriaAdded  = true;
                params.add(criteria.getEndDate());
            }
        }
    }
    
    public AuditReport get (Criteria criteria) throws APIException {
        AuditReport report = new AuditReport ();
        List <AuditRecord> records = new ArrayList <AuditRecord> ();
        logger.info("Entered");
        try {
            Integer totalRecordCount = count(criteria);
            logger.debug("totalRecordCount:" + totalRecordCount);
            report.setTotalRecordCount(totalRecordCount);
            if (!criteria.isStartAndEndRowNumSet()) {
                criteria.setStartAndEndRowNum(0, totalRecordCount);
            }
            List <Object> params = new ArrayList <Object> ();
            StringBuilder builder = new StringBuilder ();
            StringBuilder clauseBuilder = new StringBuilder ();
            builder.append ("SELECT * FROM (");
            builder.append ("   SELECT ar.*, rownum rnum FROM (");
            builder.append ("       SELECT * FROM audit_record ");
            buildQueryCriteria(criteria, params, clauseBuilder);
            builder.append (clauseBuilder.toString());
            builder.append ("       ORDER BY audit_timestamp DESC ");
            builder.append ("   ) ar ");
            builder.append (") ");
            builder.append ("WHERE rnum >= ? ");
            builder.append ("AND rnum <=  ? ");
            logger.debug("SQL:" + builder.toString());
            logger.debug("startRowNum:" + criteria.getStartRowNum());
            logger.debug("endRowNum:" + criteria.getEndRowNum());
            params.add(criteria.getStartRowNum());
            params.add(criteria.getEndRowNum());
            records = getJdbcTemplateObject().query(builder.toString(), params.toArray(), new AuditRecordMapper());
            logger.debug("records size:" + records.size());
        }
        catch (Exception expGeneral) {
            throw new APIException ("Exception getting audit records for assetId : " + criteria.getAssetId(), expGeneral);
        }
        report.setRecords(records);
        logger.info("Leaving");
        return report;
    }
    
    public List <String> getUniqueValues (String columnName) throws APIException {
        List <String> list = new ArrayList <String> ();
        logger.info("Entered");
        try {
            logger.debug("columnName:" + columnName);
            StringBuilder builder = new StringBuilder ();
            builder.append ("SELECT DISTINCT " + columnName + " FROM audit_record ");
            builder.append ("WHERE " + columnName + " IS NOT NULL ");
            builder.append ("ORDER BY " + columnName + " ASC");
            logger.debug("SQL:" + builder.toString());
            list = getJdbcTemplateObject().queryForList(builder.toString(), String.class);
            logger.debug("list size:" + list.size());
        }
        catch (Exception expGeneral) {
            throw new APIException ("Exception getting unique values for column name : " + columnName, expGeneral);
        }
        logger.info("Leaving");
        return list;
    }
    
    public JdbcTemplate getJdbcTemplateObject () throws APIException {
        logger.info("Entered");
        if (jdbcTemplateObject == null) {
            try {
                Context context = new InitialContext ();
                Context envCtx = (Context) context.lookup("java:comp/env");
                dataSource = (DataSource) envCtx.lookup("csDataSource");
                this.jdbcTemplateObject = new JdbcTemplate(dataSource);
            }
            catch (Exception expGeneral) {
                throw new APIException ("Exception instantiating datasource " + this, expGeneral);
            }
        }        
        logger.info("Leaving");
        return jdbcTemplateObject;
    }

    
    private boolean isBlank (String value) {
        if (value != null && !value.trim().equals("")) {
            return false;
        }
        return true;
    }

}
