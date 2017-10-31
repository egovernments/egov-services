ALTER TABLE egov_lcms_case_voucher RENAME COLUMN vocherType TO voucherType;

ALTER TABLE egov_lcms_case_voucher  RENAME COLUMN vocherDate TO voucherDate;


ALTER TABLE egov_lcms_case DROP COLUMN summon;
ALTER TABLE egov_lcms_case RENAME COLUMN reslovtion TO resloution;
ALTER TABLE egov_lcms_case RENAME COLUMN reslovtionDate TO resloutionDate;
ALTER TABLE egov_lcms_case  ADD COLUMN amountReceived NUMERIC;