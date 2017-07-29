CREATE SEQUENCE seq_egasset_yearwisedepreciation;

CREATE TABLE egasset_ywd
(
  id bigint NOT NULL DEFAULT nextval('seq_egasset_yearwisedepreciation'),
  depreciationrate numeric NOT NULL,
  financialyear character varying(20) NOT NULL,
  assetid bigint NOT NULL,
  usefullifeinyears bigint,
  tenantid character varying(250) NOT NULL,
  createdby character varying(64) NOT NULL,
  createddate bigint NOT NULL,
  lastmodifiedby character varying(64),
  lastmodifieddate bigint NOT NULL,
  CONSTRAINT pk_egasset_yearwisedepreciation PRIMARY KEY (id, tenantid)
);

INSERT INTO egasset_ywd(depreciationrate,financialyear,assetid,usefullifeinyears,tenantid,createdby,
	createddate,lastmodifiedby,lastmodifieddate) SELECT depreciationrate,financialyear,assetid,
		usefullifeinyears,tenantid,createdby,createddate,lastmodifiedby,lastmodifieddate FROM 
			egasset_yearwisedepreciation;

DROP TABLE egasset_yearwisedepreciation;

ALTER TABLE egasset_ywd RENAME TO egasset_yearwisedepreciation;

--rollback ALTER TABLE egasset_yearwisedepreciation RENAME TO egasset_ywd;

--rollback CREATE TABLE egasset_yearwisedepreciation
--rollback (
--rollback   depreciationrate numeric NOT NULL,
--rollback   financialyear character varying(20) NOT NULL,
--rollback   assetid bigint NOT NULL,
--rollback   usefullifeinyears bigint,
--rollback   tenantid character varying(250) NOT NULL,
--rollback   createdby character varying(64) NOT NULL,
--rollback   createddate bigint NOT NULL,
--rollback   lastmodifiedby character varying(64),
--rollback   lastmodifieddate bigint NOT NULL
--rollback );

--rollback INSERT INTO egasset_yearwisedepreciation(depreciationrate,financialyear,assetid,usefullifeinyears,
--rollback 	tenantid,createdby,createddate,lastmodifiedby,lastmodifieddate) SELECT depreciationrate,
--rollback 		financialyear,assetid,usefullifeinyears,tenantid,createdby,createddate,lastmodifiedby,
--rollback 			lastmodifieddate FROM egasset_ywd;

--rollback DROP TABLE egasset_ywd;

--rollback DELETE SEQUENCE seq_egasset_yearwisedepreciation;