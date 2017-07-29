alter table egcl_receiptinstrument drop column instrumentheader;

alter table egcl_receiptinstrument add column instrumentheader character varying(250) NOT NULL DEFAULT 'rcptinstrmnt';
alter table egcl_receiptinstrument alter column instrumentheader DROP DEFAULT;