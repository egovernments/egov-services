ALTER TABLE eglams_agreement ADD COLUMN courtcase_no character varying(64) ;
ALTER TABLE eglams_agreement ADD COLUMN courtcase_date timestamp without time zone;
ALTER TABLE eglams_agreement ADD COLUMN courtfixed_rent numeric(12,2) ;
ALTER TABLE eglams_agreement ADD COLUMN effective_date timestamp without time zone;
ALTER TABLE eglams_agreement ADD COLUMN judgement_no character varying(64) ;
ALTER TABLE eglams_agreement ADD COLUMN judgement_date timestamp without time zone;
ALTER TABLE eglams_agreement ADD COLUMN judgement_rent numeric(12,2);
