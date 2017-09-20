alter table egwtr_metercost drop constraint fk_metercost_pipe;
alter table egwtr_metercost drop column pipesizeid;
alter table egwtr_metercost drop constraint unq_metercost_metermake;
alter table egwtr_metercost add constraint unq_name_amount_tenant UNIQUE (metermake,amount,tenantid);
