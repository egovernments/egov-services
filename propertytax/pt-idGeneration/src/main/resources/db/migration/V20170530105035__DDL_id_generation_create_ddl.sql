CREATE TABLE id_generator
(
    id serial NOT NULL,
    idtype character varying(200) NOT NULL,
    entity character varying(200) NOT NULL,
    tenentid character varying(200) NOT NULL,
    regex character varying(200) NOT NULL,
    currentseqnum integer NOT NULL,
    CONSTRAINT id_generator_pkey PRIMARY KEY (id)
);

