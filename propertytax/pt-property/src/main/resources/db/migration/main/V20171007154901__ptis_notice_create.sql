CREATE TABLE egpt_notice(
    tenantid character varying NOT NULL,
    upicnumber character varying NOT NULL,
    applicationnumber character varying,
    noticedate character varying NOT NULL,
    noticenumber character varying NOT NULL,
    noticetype character varying NOT NULL,
    fileStoreId character varying NOT NULL,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    constraint uk_egpt_notice unique (tenantid,upicnumber,noticenumber,noticetype)
);
CREATE SEQUENCE seq_egpt_notice;
