ALTER TABLE egw_scheduleofrate ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_sorrate ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_marketrate ADD COLUMN deleted boolean DEFAULT false;

ALTER TABLE egw_estimatetemplate ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_estimatetemplateactivities ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_nonsor ADD COLUMN deleted boolean DEFAULT false;
