alter table egwtr_servicecharge add column active boolean NOT NULL default true;
alter table egwtr_servicecharge alter column active drop default;