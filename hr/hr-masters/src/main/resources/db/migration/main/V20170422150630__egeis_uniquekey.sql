alter table egeis_grade add CONSTRAINT uk_eg_grade_name_tenantid UNIQUE (name,tenantid);

alter table egeis_group add CONSTRAINT uk_eg_group_name_tenantid UNIQUE (name,tenantid);

alter table egeis_recruitmentMode add CONSTRAINT uk_egeis_recruitmentMode_name_tenantid UNIQUE (name,tenantid);

alter table egeis_recruitmentType add CONSTRAINT uk_egeis_recruitmentType_name_tenantid UNIQUE (name,tenantid);
