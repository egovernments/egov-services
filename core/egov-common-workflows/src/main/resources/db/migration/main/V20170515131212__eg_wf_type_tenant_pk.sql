alter table EG_WF_TYPES drop constraint pk_EG_WF_TYPES ;
alter table EG_WF_TYPES add constraint pk_EG_WF_TYPES primary key (id,tenantId);