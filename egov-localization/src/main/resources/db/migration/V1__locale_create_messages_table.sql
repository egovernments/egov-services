CREATE TABLE message (
	id serial not null primary key,
	locale varchar(255) not null,
	code varchar(255) not null,
	message varchar(500) not null,
	tenant_id varchar(500) not null,
	CONSTRAINT unique_message_entry UNIQUE(locale, code, tenant_id)
);