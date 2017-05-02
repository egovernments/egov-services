CREATE TABLE egpgr_receiving_center (
    id bigint primary key ,
    name character varying(100),
    iscrnrequired boolean DEFAULT false,
    orderno bigint DEFAULT 0,
    version bigint DEFAULT 0,
    tenantid character varying(256) not null,
    constraint uk_receivingcenter_name_tenant unique (name,tenantid)
);

CREATE SEQUENCE seq_egpgr_receiving_center
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    