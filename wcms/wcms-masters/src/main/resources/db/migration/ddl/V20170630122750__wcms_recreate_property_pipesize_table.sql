drop table if exists egwtr_property_pipe_size;

CREATE TABLE egwtr_property_pipe_size
(
  id bigint NOT NULL,
  pipesizeid bigint NOT NULL,
  propertytypeid character varying(50) NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_property_pipe_size_pkey PRIMARY KEY (id,tenantid),
 CONSTRAINT unq_property_pipesize UNIQUE (pipesizeid,propertytypeid,tenantid),
  CONSTRAINT fk_pipesizeid FOREIGN KEY (pipesizeid,tenantid) REFERENCES egwtr_pipesize (id,tenantid) 

);