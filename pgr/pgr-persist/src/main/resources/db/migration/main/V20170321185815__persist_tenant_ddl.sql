alter table egpgr_complaint add column tenantid character varying(256) not null default 'ap.public';
alter table egpgr_complaint add constraint uk_complaint_crn_tenant unique (crn,tenantid);

alter table egpgr_complainant add column tenantid character varying(256) not null default 'ap.public';
alter table egpgr_complainant add constraint uk_complainant_id_tenant unique (id,tenantid);

alter table egpgr_complaintstatus add column tenantid character varying(256) not null default 'ap.public';
alter table egpgr_complaintstatus add constraint uk_complaintstatus_name_tenant unique (name,tenantid);

alter table egpgr_complainttype add column tenantid character varying(256) not null default 'ap.public';
alter table egpgr_complainttype add constraint unique_complainttype_code_tenant unique (code, tenantid);

alter table egpgr_complainttype_category add column tenantid character varying(256) not null default 'ap.public';
alter table egpgr_complainttype_category add constraint unique_complainttypecategory_name_tenant unique (name, tenantid);

alter table egpgr_escalation add column tenantid character varying(256) not null default 'ap.public';
alter table egpgr_escalation add constraint uk_escalation_id_tenant unique (id,tenantid);

alter table egpgr_receiving_center add column tenantid character varying(256) not null default 'ap.public';
alter table egpgr_receiving_center add constraint uk_receivingcenter_name_tenant unique (name,tenantid);

alter table egpgr_receivingmode add column tenantid character varying(256) not null default 'ap.public';
alter table egpgr_receivingmode add constraint uk_receivingmode_code_tenant unique (code,tenantid);

alter table egpgr_supportdocs add column tenantid character varying(256) not null default 'ap.public';
alter table egpgr_supportdocs add constraint uk_supportdocs_filestoreid_complaintid_tenant unique (filestoreid,complaintid,tenantid);