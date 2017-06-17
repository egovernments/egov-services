ALTER TABLE egpgr_complainttype_category
drop column code;	

ALTER TABLE egpgr_complainttype_category
ADD code varchar(255) NOT NULL default 'CMPL';	
