
DROP TABLE egpgr_receiving_center;
DROP TABLE egpgr_receivingmode;

DROP sequence seq_egpgr_receiving_center;
DROP sequence seq_egpgr_receivingmode; 


CREATE TABLE egpgr_receiving_center (
    id bigint NOT NULL,
    name character varying(100),
    iscrnrequired boolean DEFAULT false,
    orderno bigint DEFAULT 0,
    version bigint DEFAULT 0,
    tenantid character varying(256) not null,
    code character varying(100) NOT NULL,
    description character varying(250) NULL,
    active boolean DEFAULT true,
    createdby bigint,
    createddate timestamp without time zone,
 	lastmodifiedby bigint,
 	lastmodifieddate timestamp without time zone,
 	CONSTRAINT egpgr_receivingcenter_pkey PRIMARY KEY (id),
 	CONSTRAINT egpgr_receivingcenter_un UNIQUE (code, tenantid)
 
);

CREATE SEQUENCE seq_egpgr_receiving_center
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

create table egpgr_receivingmode(
id bigint NOT NULL,
name varchar(150),
code varchar(50) not null,
version bigint default 0,
tenantid character varying(256) not null,
description character varying(250) NULL,
active boolean DEFAULT true,
createdby bigint,
createddate timestamp without time zone,
lastmodifiedby bigint,
lastmodifieddate timestamp without time zone,
channel varchar(255) NOT NULL,
CONSTRAINT egpgr_receivingmode_pkey PRIMARY KEY (id),
CONSTRAINT egpgr_receivingMode_check CHECK (channel IN ('WEB', 'MOBILE','WEB,MOBILE','MOBILE,WEB')),
constraint egpgr_receivingmode_un unique (code,tenantid)

);

CREATE SEQUENCE seq_egpgr_receivingmode
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

