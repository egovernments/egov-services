ALTER TABLE eglcms_advocate_payment RENAME COLUMN reslovtion TO resolution;
ALTER TABLE eglcms_advocate_payment DROP COLUMN reslovtionDate;

ALTER TABLE eglcms_advocate_payment DROP receiptdate;
ALTER TABLE eglcms_advocate_payment DROP remarks;
ALTER TABLE eglcms_advocate_payment DROP advocateinfodate;
ALTER TABLE eglcms_advocate_payment DROP pleaderengagementdetails;

ALTER TABLE eglcms_advocate_payment RENAME TO egov_lcms_advocate_payment;

ALTER TABLE egov_lcms_advocate_payment ADD COLUMN voucherNo character varying;
ALTER TABLE egov_lcms_advocate_payment ADD COLUMN voucherDate bigint;

ALTER TABLE egov_lcms_hearing_details
ALTER COLUMN nexthearingtime TYPE character varying USING nexthearingtime::character varying;

ALTER TABLE egov_lcms_case ALTER COLUMN hearingtime TYPE character varying USING hearingtime::character varying;