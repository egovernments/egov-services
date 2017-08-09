alter table  egwtr_metercost drop column createddate;
alter table  egwtr_metercost drop column lastmodifieddate;

alter table egwtr_metercost add column createddate bigint;
alter table egwtr_metercost add column lastmodifieddate bigint;
alter table egwtr_metercost add column code character varying(50) NOT NULL;

alter table egwtr_metercost alter column lastmodifiedby SET NOT NULL ;
alter table egwtr_metercost drop constraint fk_metercost_pipesize;


alter table egwtr_metercost drop column pipesize;

alter table egwtr_metercost add column pipesizeid bigint;

alter table egwtr_metercost add constraint fk_metercost_pipe FOREIGN KEY (pipesizeid,tenantid)
REFERENCES egwtr_pipesize(id,tenantid);


