CREATE TABLE submission
(
  crn VARCHAR(32) NOT NULL,
  tenantid VARCHAR(256) NOT NULL,
  escalationdate timestamp,
  landmarkdetails VARCHAR(200),
  department bigint,
  assignee bigint,
  latitude double precision,
  longitude double precision,
  status VARCHAR(25),
  details VARCHAR(500) NOT NULL,
  servicecode VARCHAR(20) NOT NULL,
  email character varying(100),
  mobile character varying(20),
  name character varying(150),
  loggedinrequester bigint,
  requesteraddress character varying(256),
  createddate timestamp NOT NULL ,
  lastmodifieddate timestamp ,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version bigint,
  CONSTRAINT pk_submission PRIMARY KEY (crn, tenantid)
);

CREATE TABLE submission_attribute
(
  id bigint NOT NULL,
  crn VARCHAR(32) NOT NULL,
  tenantid VARCHAR(256) NOT NULL,
  code VARCHAR(50) NOT NULL,
  key VARCHAR(50) NOT NULL,
  version bigint DEFAULT 0,
  createddate timestamp NOT NULL,
  lastmodifieddate timestamp ,
  createdby bigint NOT NULL,
  lastmodifiedby bigint
);

ALTER TABLE submission_attribute ADD CONSTRAINT submission_attribute_pk PRIMARY KEY(id);

CREATE SEQUENCE seq_submission_attribute
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



-- Move this to attribute table
--   receivingmode smallint,
--   receivingcenter bigint,
--   state_id bigint,
--   citizenFeedback bigint,
--   crossHierarchyId bigint,
--   childlocation bigint,