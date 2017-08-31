
--taxperiod

insert into egbs_taxperiod 
(id, service, code, fromdate, todate, financialyear, createddate, lastmodifieddate, createdby, lastmodifiedby, tenantid, periodcycle) values (nextval('seq_egbs_taxperiod'), 'Marriage Regn', 'MR_FEE', 1427826600, 1443637799, '2015-16', 1500118803611, 1500118803611, 1, 1, 'default', 'HALFYEAR');


--egbs_business_service_details

insert into egbs_business_service_details
 (id, businessservice, collectionmodesnotallowed, callbackforapportioning, partpaymentallowed, callbackapportionurl, createddate, lastmodifieddate, createdby, lastmodifiedby, tenantid) values 
(nextval('seq_egbs_business_srvc_details'), 'Marriage Regn', '', false, true, '/billing-service/bill/_apportion', 1500118803611, 1500118803611, 1, 1, 'default');


--egbs_glcodemaster

insert into egbs_glcodemaster
 (id, tenantid, taxhead, service, fromdate, todate, createdby, createdtime, lastmodifiedby, lastmodifiedtime,glcode) values
 (nextval('seq_egbs_glcodemaster'), 'default', 'MR_FEE', 'Marriage Regn', 1427826600, 1601490599, 1, 1500118803611, 1, 1500118803611, '1100101');

--egbs_taxheadmaster

insert into egbs_taxheadmaster 
(id, tenantid, category, service, name, code, isdebit, isactualdemand, orderno, validfrom, validtill, createdby, createdtime, lastmodifiedby, lastmodifiedtime) values 
(nextval('seq_egbs_taxheadmaster'), 'default', 'FEE', 'Marriage Regn', 'Marriage Regn', 'MR_FEE', false, true, 1, 1427826600, 1601490599, 1, 1500118803611, 1, 1500118803611);


