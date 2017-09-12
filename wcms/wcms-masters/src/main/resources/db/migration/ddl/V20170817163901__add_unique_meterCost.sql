alter table egwtr_metercost add CONSTRAINT unq_metercost_metermake UNIQUE (metermake,tenantid);
alter table egwtr_metercost add CONSTRAINT unq_metercost_code UNIQUE (code,tenantid);