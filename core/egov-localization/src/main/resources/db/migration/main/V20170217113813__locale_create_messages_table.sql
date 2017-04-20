CREATE TABLE message (
	id serial not null primary key,
	locale varchar(255) not null,
	code varchar(255) not null,
	message varchar(500) not null,
	tenantid character varying(256) not null,
	constraint unique_message_entry unique (locale, code, tenantid)
);