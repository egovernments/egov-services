ALTER TABLE eglams_agreement ADD COLUMN res_category bigint;
ALTER TABLE eglams_agreement ADD COLUMN base_allotment character varying(128);

ALTER TABLE eglams_agreement ADD CONSTRAINT fk_res_category FOREIGN KEY (res_category) references eglams_reservation_category (id);
