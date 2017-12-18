alter table pricelist alter column agreementDate type bigint using agreementDate::bigint;

alter table pricelist alter column agreementStartDate type bigint using agreementstartdate::bigint;

alter table pricelist alter column agreementEndDate type bigint using agreementEndDate::bigint;

alter table pricelistdetails alter column fromDate type bigint using fromDate::bigint;

alter table pricelistdetails alter column toDate type bigint using toDate::bigint;

