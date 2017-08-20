alter table egwtr_waterconnection add column plumbername character varying(100);

alter table egwtr_waterconnection add column billsequencenumber bigint NOT NULL DEFAULT 0;

alter table egwtr_waterconnection add column outsideulb boolean NOT NULL DEFAULT false;

alter table egwtr_waterconnection add column meterowner character varying(50);

alter table egwtr_waterconnection add column metermodel character varying(50);
