
alter table egwtr_waterconnection drop column if exists demandid; 

alter table egwtr_waterconnection add column   demandid character varying(100); 