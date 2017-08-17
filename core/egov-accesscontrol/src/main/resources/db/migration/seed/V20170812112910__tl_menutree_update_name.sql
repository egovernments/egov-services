update service set name ='License Masters' where code='TLMASTERS' and tenantId='default';
update service set name ='License Sub Category', displayname='License Sub Category' where code='TLSUBCATEGORY' and tenantId='default';
update service set name ='License Unit of Measurement' where code='TLUOM' and tenantId='default';
update service set name ='License Document Type' where code='TLDOCUMENTTYPE' and tenantId='default';
update service set name ='License Penalty Rate' where code='TLPENALTYRATE' and tenantId='default';
update service set name ='License Fee Matrix' where code='TLFEEMATRIX' and tenantId='default';

update service set name ='License Transactions',ordernumber=2 where code='TLTRANSACTIONS' and tenantId='default';
update service set name ='License Search',ordernumber=3 where code='TLSEARCH' and tenantId='default';
