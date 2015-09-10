--CREATE USER sites_audit IDENTIFIED BY welcome1;
--grant all privileges to sites_audit;

DROP TABLE audit_record;
CREATE TABLE audit_record (
    audit_id            NUMBER PRIMARY KEY NOT NULL,
    asset_id            NUMBER NOT NULL,
    asset_type          VARCHAR2 (50) NOT NULL,
    asset_name          VARCHAR2 (200) NOT NULL,
    target_asset_id     NUMBER NULL,
    target_asset_type   VARCHAR2 (50) NULL,
    target_asset_name   VARCHAR2 (200) NULL,
    user_id             VARCHAR2 (50) NOT NULL,
    operation           VARCHAR2 (20) NOT NULL,
    audit_timestamp     TIMESTAMP NOT NULL
);

DROP SEQUENCE audit_seq;
CREATE SEQUENCE audit_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
