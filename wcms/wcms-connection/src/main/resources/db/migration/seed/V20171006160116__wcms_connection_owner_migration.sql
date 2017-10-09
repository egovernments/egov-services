INSERT INTO egwtr_connection_owners (
select nextval('seq_egwtr_connection_owners'), id, userid,true, 1 , tenantid, NULLIF(createdby, '')::int, NULLIF(lastmodifiedby, '')::int ,
extract(epoch from createdtime), extract(epoch from lastmodifiedtime), 0 from egwtr_waterconnection where propertyidentifier is null
and userid is not null and not exists (select b.* from egwtr_connection_owners b where b.waterconnectionid=egwtr_waterconnection.id)); 