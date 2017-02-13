CREATE TABLE MESSAGES (
	id serial not null primary key,
	locale varchar(255) not null,
	code varchar(255) not null,
	message varchar(500) not null,
	jurisdiction_id varchar(500) not null,
	CONSTRAINT unique_message_entry UNIQUE(locale, code, jurisdiction_id)
);