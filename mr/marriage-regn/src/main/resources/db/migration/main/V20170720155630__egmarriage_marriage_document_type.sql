create sequence seq_marriage_document_type;
create sequence seq_marriage_document_type_code;

create table egmr_marriage_document_type(
	id BIGINT,
	name character varying(250),
	code character varying(250),
	isactive Boolean,
	isindividual Boolean,
	isrequired Boolean,
	proof character varying(250),
	appltype character varying(250),
	tenantid character varying(250),
	CONSTRAINT pkey_id_tenantid PRIMARY KEY(id,tenantid)
)
