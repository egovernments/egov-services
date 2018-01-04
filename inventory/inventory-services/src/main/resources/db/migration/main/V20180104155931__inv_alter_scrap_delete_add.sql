ALTER TABLE scrap ADD COLUMN deleted boolean default false;
ALTER TABLE scrapdetail ADD COLUMN deleted boolean default false;
ALTER TABLE scrapdetail ADD COLUMN existingValue numeric (13,2);
ALTER TABLE scrapdetail ADD COLUMN userQuantity numeric (13,2);
