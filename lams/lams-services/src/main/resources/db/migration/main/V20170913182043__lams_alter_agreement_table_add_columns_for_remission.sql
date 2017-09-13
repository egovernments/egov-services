ALTER TABLE eglams_agreement ADD COLUMN remission_fee numeric(12,2) ;
ALTER TABLE eglams_agreement ADD COLUMN remission_from_date timestamp without time zone;
ALTER TABLE eglams_agreement ADD COLUMN remission_to_date timestamp without time zone;
ALTER TABLE eglams_agreement ADD COLUMN remission_order_no character varying(64);
