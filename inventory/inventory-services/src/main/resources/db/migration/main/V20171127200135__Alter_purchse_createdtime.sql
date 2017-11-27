
alter table purchaseorder drop column createdTime;
alter table purchaseorder add column createdTime bigint;

alter table purchaseindentdetail drop column createdTime;
alter table purchaseindentdetail add column createdTime bigint;


alter table purchaseorder drop column lastModifiedTime;
alter table purchaseorder add column lastModifiedTime bigint;

alter table purchaseindentdetail drop column lastModifiedTime;
alter table purchaseindentdetail add column lastModifiedTime bigint;


