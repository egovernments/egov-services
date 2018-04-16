alter table egeis_leavetype drop CONSTRAINT uk_egeis_leavetype_name;
alter table egeis_leavetype add CONSTRAINT uk_egeis_leaveType_name_tenantid UNIQUE (name,tenantid);