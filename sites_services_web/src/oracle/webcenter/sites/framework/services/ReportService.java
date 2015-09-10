package oracle.webcenter.sites.framework.services;

import java.io.Serializable;

import oracle.webcenter.sites.framework.exceptions.APIException;
import oracle.webcenter.sites.framework.model.AuditReport;

public interface ReportService extends Serializable {
    public byte [] generate (AuditReport auditReport) throws APIException;
}
