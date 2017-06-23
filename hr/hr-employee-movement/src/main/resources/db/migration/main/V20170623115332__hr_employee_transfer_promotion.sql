CREATE TABLE egeis_transfer_reason (
	id BIGINT NOT NULL,
	description CHARACTER VARYING(250)
);

CREATE SEQUENCE seq_egeis_transfer_reason
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE egeis_promotion_basis (
	id BIGINT NOT NULL,
	description CHARACTER VARYING(250)
);

CREATE SEQUENCE seq_egeis_promotion_basis
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;