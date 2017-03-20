create sequence seq_egpgr_receivingmode;

create table egpgr_receivingmode(
id bigint primary key,
name varchar(150),
code varchar(50),
visible boolean,
version bigint default 0
);