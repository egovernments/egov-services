update egasset_statuses  set name = 'CREATED' where objectname = 'Asset Master' and code = 'CREATED';
update egasset_statuses  set name = 'CAPITALIZED' where objectname = 'Asset Master' and code = 'CAPITALIZED';
update egasset_statuses  set name = 'CANCELLED' where objectname = 'Asset Master' and code = 'CANCELLED';
update egasset_statuses  set name = 'DISPOSED' where objectname = 'Asset Master' and code = 'DISPOSED';
		
update egasset_statuses  set name = 'CREATED' where objectname = 'Revaluation' and code = 'CREATED';
update egasset_statuses  set name = 'APPROVED' where objectname = 'Revaluation' and code = 'APPROVED';
update egasset_statuses  set name = 'CANCELLED' where objectname = 'Revaluation' and code = 'CANCELLED';
		
update egasset_statuses  set name = 'CREATED' where objectname = 'Disposal' and code = 'CREATED';
update egasset_statuses  set name = 'APPROVED' where objectname = 'Disposal' and code = 'APPROVED';
update egasset_statuses  set name = 'CANCELLED' where objectname = 'Disposal' and code = 'CANCELLED';
		
		
--rollback update egasset_statuses set name = null;