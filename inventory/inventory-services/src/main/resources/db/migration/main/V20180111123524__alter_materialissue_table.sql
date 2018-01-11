alter table materialissue add column scrapCreated  boolean default false;
alter table materialissuedetail add column scrapedQuantity numeric(18,6);


