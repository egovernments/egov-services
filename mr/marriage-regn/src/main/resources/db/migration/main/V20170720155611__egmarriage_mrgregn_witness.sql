
CREATE SEQUENCE seq_marriageregn_witness;

CREATE TABLE egmr_marriageregn_witness
(
   id bigint NOT NULL,
  applicationnumber character varying(250) NOT NULL,
  tenantid character varying(250) NOT NULL,
  witnessno integer NOT NULL,
  name character varying(250) NOT NULL,
  relation character varying(250) NOT NULL,
  relatedto character varying(250) NOT NULL,
  address character varying(250) NOT NULL,
  relationshipwithapplicants character varying(250) NOT NULL,
  occupation character varying(250),
  aadhaar character varying(250),
  mobileno character varying(250),
  email character varying(250),
  dob bigint,
  photo character varying,
  CONSTRAINT egmr_marriageregn_witness_pkey PRIMARY KEY (id),
  CONSTRAINT fk_egmr_witness_applicationnumber FOREIGN KEY (applicationnumber)
      REFERENCES public.egmr_marriage_regn (applicationnumber) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)