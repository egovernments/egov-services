ALTER TABLE egpt_notice ALTER COLUMN noticedate TYPE timestamp USING CAST (TO_DATE(noticedate, 'DD/MM/YYYY') AS
TIMESTAMP);