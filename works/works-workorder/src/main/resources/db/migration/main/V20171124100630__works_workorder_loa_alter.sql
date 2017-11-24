alter table egw_securitydeposit add column id varchar(256);
alter table egw_securitydeposit add constraint unique_egw_securitydeposit unique  (id,tenantid);