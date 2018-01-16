
ALTER TABLE eglams_history ADD COLUMN resolutionno character varying(64);
ALTER TABLE eglams_history ADD COLUMN resolutiondate timestamp without time zone;
ALTER TABLE eglams_history ADD COLUMN createddate timestamp without time zone;
ALTER TABLE eglams_history ADD COLUMN createdby character varying(64);
ALTER TABLE eglams_history ADD COLUMN lastmodifieddate timestamp without time zone;
ALTER TABLE eglams_history ADD COLUMN lastmodifiedby character varying(64);
