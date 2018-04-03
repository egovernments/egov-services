ALTER TABLE eglams_agreement ADD COLUMN tenderopeningdate timestamp without time zone;
ALTER TABLE eglams_agreement ADD COLUMN auctionamount numeric(12,2) default 0;
ALTER TABLE eglams_agreement ADD COLUMN solvencyamount numeric(12,2) default 0;
