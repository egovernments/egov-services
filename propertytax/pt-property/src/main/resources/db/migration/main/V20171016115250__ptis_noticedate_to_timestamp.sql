ALTER TABLE egpt_notice ALTER COLUMN noticedate TYPE timestamp USING noticedate::timestamp without time zone;
