 update egasset_asset set status='CREATED' where status !='CAPITALIZED';

 --rollback update egasset_asset set status='CAPITALIZED' where status !='CREATED';

