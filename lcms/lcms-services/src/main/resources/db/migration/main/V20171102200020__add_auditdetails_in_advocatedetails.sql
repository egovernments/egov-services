ALTER TABLE egov_lcms_case_advocate RENAME COLUMN assigndate TO assigneddate;

ALTER TABLE egov_lcms_case_advocate 
  ADD COLUMN tenantid character varying,
  ADD COLUMN createdby character varying,
  ADD COLUMN lastmodifiedby character varying,
  ADD COLUMN createdtime bigint,
  ADD COLUMN lastmodifiedtime bigint;