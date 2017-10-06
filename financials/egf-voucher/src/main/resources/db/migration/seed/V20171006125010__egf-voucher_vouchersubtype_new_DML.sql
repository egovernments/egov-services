delete from egf_vouchersubtype;

insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('1','STANDARD_VOUCHER_TYPE_CONTRA','BankToBank','CSL',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('2','STANDARD_VOUCHER_TYPE_CONTRA','BankToCash','CSL',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('3','STANDARD_VOUCHER_TYPE_CONTRA','CashToBank','CSL',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('4','STANDARD_VOUCHER_TYPE_CONTRA','Pay in slip','CSL',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('5','STANDARD_VOUCHER_TYPE_CONTRA','InterFundTransfer','CSL',null,'false',1,now(),1,now(),'default');

insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('6','STANDARD_VOUCHER_TYPE_PAYMENT','Bill Payment','BPV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('7','STANDARD_VOUCHER_TYPE_PAYMENT','Advance Payment','BPV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('8','STANDARD_VOUCHER_TYPE_PAYMENT','Direct Bank Payment','BPV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('9','STANDARD_VOUCHER_TYPE_PAYMENT','Remittance Payment','BPV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('10','STANDARD_VOUCHER_TYPE_PAYMENT','Salary Bill Payment','BPV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('11','STANDARD_VOUCHER_TYPE_PAYMENT','Pension Bill Payment','BPV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('12','STANDARD_VOUCHER_TYPE_PAYMENT','Bank Entry','BPV',null,'false',1,now(),1,now(),'default');

insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('13','STANDARD_VOUCHER_TYPE_JOURNAL','JVGeneral','GJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('14','STANDARD_VOUCHER_TYPE_JOURNAL','Issue','GJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('15','STANDARD_VOUCHER_TYPE_JOURNAL','Supplier Journal','PJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('16','STANDARD_VOUCHER_TYPE_JOURNAL','Supplier Receipt','GJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('17','STANDARD_VOUCHER_TYPE_JOURNAL','Contractor Journal','CSL',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('18','STANDARD_VOUCHER_TYPE_JOURNAL','Salary Journal','SJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('19','STANDARD_VOUCHER_TYPE_JOURNAL','Fixedasset Journal','FJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('20','STANDARD_VOUCHER_TYPE_JOURNAL','Purchase Journal','PJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('21','STANDARD_VOUCHER_TYPE_JOURNAL','Expense Journal','EJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('22','STANDARD_VOUCHER_TYPE_JOURNAL','Pension Journal','TJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('23','STANDARD_VOUCHER_TYPE_JOURNAL','LE-Demand-Voucher','GJV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('24','STANDARD_VOUCHER_TYPE_JOURNAL','Receipt Reversal','GJV',null,'false',1,now(),1,now(),'default');

insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('25','STANDARD_VOUCHER_TYPE_RECEIPT','Direct','BRV',null,'false',1,now(),1,now(),'default');

insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('27','STANDARD_VOUCHER_TYPE_RECEIPT','Payment Reversal','BRV',null,'false',1,now(),1,now(),'default');
insert into egf_vouchersubtype (id,vouchertype,vouchername,vouchernameprefix,cutoffdate,exclude,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values('28','STANDARD_VOUCHER_TYPE_RECEIPT','Other Receipts','BRV',null,'false',1,now(),1,now(),'default');