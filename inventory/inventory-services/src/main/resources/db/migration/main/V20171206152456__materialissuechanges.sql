alter table materialissuedetail alter column indentdetailid drop NOT NULL;
alter table materialissuedetail rename column materialid to materialcode;
ALTER TABLE materialissue ADD COLUMN deleted boolean;