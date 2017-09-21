ALTER TABLE eglams_agreement ADD COLUMN adjustment_start_date timestamp without time zone;

COMMENT ON COLUMN eglams_agreement.adjustment_start_date IS 'Starting date to adjust the advance collection'; 

