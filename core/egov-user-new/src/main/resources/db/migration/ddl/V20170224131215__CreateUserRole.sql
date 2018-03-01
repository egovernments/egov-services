CREATE TABLE eg_userrole
(
  rolecode character varying(100) NOT NULL,
  userid bigint NOT NULL,
  tenantid character varying(256) NOT NULL,
  lastmodifieddate bigint,
  CONSTRAINT eg_userrole_roleid_fkey FOREIGN KEY (rolecode,tenantid)
      REFERENCES eg_role (code,tenantid),
  CONSTRAINT eg_userrole_userid_fkey FOREIGN KEY (userid,tenantid)
      REFERENCES eg_user (id, tenantid)
)