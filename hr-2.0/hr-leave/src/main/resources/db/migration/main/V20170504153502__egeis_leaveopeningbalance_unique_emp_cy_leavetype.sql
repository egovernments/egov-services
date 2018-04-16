alter table egeis_leaveopeningbalance add constraint egeis_lop_emp_cy_lt unique(employeeid,calendaryear,leavetypeid,tenantid);
