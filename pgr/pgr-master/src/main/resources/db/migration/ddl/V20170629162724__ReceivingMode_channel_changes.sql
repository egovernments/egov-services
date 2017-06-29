CREATE TABLE public.egpgr_receivingmode_channel
(
  id bigint NOT NULL,
  receivingmodecode character varying(50) NOT NULL,
  channel character varying(255) NOT NULL,
  CONSTRAINT egpgr_receivingmode_channel_pkey PRIMARY KEY (id),
  CONSTRAINT egpgr_receivingmode_ukey UNIQUE (receivingmodecode, channel)
);

CREATE SEQUENCE seq_egpgr_receivingmode_channel;


