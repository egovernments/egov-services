

CREATE TABLE eg_servicetypeconfiguration(
   servicecode character varying(100) not null,
   applicationfeesenabled boolean,
   notificationenabled boolean,
   slaenabled   boolean,
  glCode  character varying(20),
  online boolean,
  source   character varying(100),
  url   character varying(256),
  tenantid  character varying(256) not null,
  CONSTRAINT service_code_ukey UNIQUE (servicecode,tenantid));
