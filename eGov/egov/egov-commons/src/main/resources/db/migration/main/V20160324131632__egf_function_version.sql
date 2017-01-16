alter table function add column version numeric;

alter table function add column createddate timestamp without time zone;

alter table function add column lastmodifiedby bigint;

alter table function add column lastmodifieddate timestamp without time zone;

update function set version=0 where version is null;

