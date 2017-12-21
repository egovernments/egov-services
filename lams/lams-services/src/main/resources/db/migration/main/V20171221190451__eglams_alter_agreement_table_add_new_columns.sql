ALTER TABLE eglams_agreement ADD COLUMN first_allotment character varying(16) ;
ALTER TABLE eglams_agreement ADD COLUMN gstin character varying(16) NOT NULL;
ALTER TABLE eglams_agreement ADD COLUMN municipal_order_no character varying(32);
ALTER TABLE eglams_agreement ADD COLUMN municipal_order_date timestamp without time zone;
ALTER TABLE eglams_agreement ADD COLUMN govt_order_no character varying(32);
ALTER TABLE eglams_agreement ADD COLUMN govt_order_date timestamp without time zone;

