CREATE TABLE egpa_search_config
(
  id bigint NOT NULL,
  tenant character varying(1) NOT NULL,
  kpi character varying(1) NOT NULL,
  finyear character varying(1) NOT NULL,
  possibility character varying(3) NOT NULL,
  CONSTRAINT egpa_search_config_pk PRIMARY KEY (id))
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_search_config;

INSERT INTO egpa_search_config VALUES (nextval('seq_egpa_search_config')
, '1', '1', '1', 'YES');

INSERT INTO egpa_search_config VALUES (nextval('seq_egpa_search_config')
, '1', '1', '*', 'YES');

INSERT INTO egpa_search_config VALUES (nextval('seq_egpa_search_config')
, '1', '*', '1', 'YES');

INSERT INTO egpa_search_config VALUES (nextval('seq_egpa_search_config')
, '1', '*', '*', 'NO');

INSERT INTO egpa_search_config VALUES (nextval('seq_egpa_search_config')
, '*', '1', '1', 'YES');

INSERT INTO egpa_search_config VALUES (nextval('seq_egpa_search_config')
, '*', '1', '*', 'YES');

INSERT INTO egpa_search_config VALUES (nextval('seq_egpa_search_config')
, '*', '*', '1', 'NO');

INSERT INTO egpa_search_config VALUES (nextval('seq_egpa_search_config')
, '*', '*', '*', 'NO');

