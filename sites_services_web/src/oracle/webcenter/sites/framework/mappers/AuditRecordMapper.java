package oracle.webcenter.sites.framework.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;

import oracle.webcenter.sites.framework.model.Asset;
import oracle.webcenter.sites.framework.model.AuditRecord;

import org.springframework.jdbc.core.RowMapper;

public class AuditRecordMapper implements RowMapper<AuditRecord> {
    public AuditRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setId(rs.getLong("AUDIT_ID"));
        auditRecord.setOperation(rs.getString("OPERATION"));
        auditRecord.setUserId(rs.getString("USER_ID"));
        Asset sourceAsset = new Asset (rs.getLong("ASSET_ID"), rs.getString("ASSET_TYPE"));
        sourceAsset.setName(rs.getString("ASSET_NAME"));
        auditRecord.setSourceAsset(sourceAsset);
        Long targetId = rs.getLong("TARGET_ASSET_ID");
        if (targetId != null ) {
            Asset targetAsset = new Asset (targetId.longValue(), rs.getString("TARGET_ASSET_TYPE"));
            targetAsset.setName(rs.getString("TARGET_ASSET_NAME"));
            auditRecord.setTargetAsset(targetAsset);
        }
        auditRecord.setTimestamp(new Date (rs.getTimestamp("AUDIT_TIMESTAMP").getTime()));
        return auditRecord;
    }
}
