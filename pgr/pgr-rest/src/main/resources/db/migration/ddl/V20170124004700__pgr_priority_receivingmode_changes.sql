create table egpgr_receivingmode(
id bigint primary key,
name varchar(150),
code varchar(50),
visible boolean,
version bigint default 0,
tenantid character varying(256) not null,
constraint uk_receivingmode_code_tenant unique (code,tenantid)
);

create sequence seq_egpgr_receivingmode;
