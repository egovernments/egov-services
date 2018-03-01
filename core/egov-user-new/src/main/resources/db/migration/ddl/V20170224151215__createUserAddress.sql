CREATE TABLE public.eg_user_address
(
  type character varying(50) NOT NULL,
  address character varying(300),
  city character varying(300),
  pincode character varying(10),
  userid bigint NOT NULL,
  tenantid character varying(256) NOT NULL,
  CONSTRAINT eg_user_address_fkey FOREIGN KEY (userid, tenantid) REFERENCES eg_user (id, tenantid),
  CONSTRAINT eg_user_address_type_unique UNIQUE (userid, tenantid, type)
)