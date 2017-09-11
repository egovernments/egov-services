ALTER TABLE egtl_tradelicense_bill DROP COLUMN licenseid;
ALTER TABLE egtl_tradelicense_bill ADD COLUMN applicationid bigint NOT NULL;
ALTER TABLE egtl_tradelicense_bill ADD CONSTRAINT fk_trade_application_id FOREIGN KEY (applicationid)
      REFERENCES egtl_license_application (id);