ALTER TABLE eglams_rentincrementtype ADD COLUMN tenant_id character varying(64) NOT NULL;

ALTER TABLE eglams_agreement ALTER column tenant_id SET NOT NULL;

ALTER TABLE eglams_rentincrementtype DROP CONSTRAINT pk_eglams_rentincrementtype, ADD CONSTRAINT pk_eglams_rentincrementtype PRIMARY KEY(id,tenant_id);

ALTER TABLE eglams_agreement DROP CONSTRAINT pk_eglams_agreement, ADD CONSTRAINT pk_eglams_agreement PRIMARY KEY(id,tenant_id);

ALTER TABLE eglams_demand DROP CONSTRAINT pk_eglams_demand, ADD CONSTRAINT pk_eglams_demand PRIMARY KEY(id,tenantid);
