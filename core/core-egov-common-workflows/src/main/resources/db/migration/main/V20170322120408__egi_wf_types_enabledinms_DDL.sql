alter table eg_wf_states add mylinkid varchar(256);

alter table EG_WF_TYPES add enabledInMs boolean not null default false;
