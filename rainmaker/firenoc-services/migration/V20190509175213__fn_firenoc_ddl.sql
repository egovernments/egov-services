-- Table: public.eg_fn_firenoc

-- DROP TABLE public.eg_fn_firenoc;

CREATE TABLE eg_fn_firenoc
(
    uuid character varying(64),
    tenantid character varying(128),
    firenocnumber character varying(64),
    provisionfirenocnumber character varying(64),
    oldfirenocnumber character varying(64),
    dateofapplied character varying(256),
    createdby character varying(64),
    lastmodifiedby character varying(64),
    createdtime bigint,
    lastmodifiedtime bigint,
    CONSTRAINT uk_eg_fn_firenoc UNIQUE (uuid)

);
