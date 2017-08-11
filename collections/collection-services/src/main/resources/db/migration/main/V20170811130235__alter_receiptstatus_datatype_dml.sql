alter table egcl_receiptheader drop column status;

alter table egcl_receiptheader add column status character varying(50) NOT NULL DEFAULT 'TO BE SUBMITTED';