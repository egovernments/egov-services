CREATE TABLE data_sync_epoch (
    id serial primary key,
    epoch timestamp without time zone not null
);

CREATE SEQUENCE seq_data_sync_epoch
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
