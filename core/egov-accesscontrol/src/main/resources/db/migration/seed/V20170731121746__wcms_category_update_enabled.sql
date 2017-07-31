update eg_action set name='View Category Type' where name='SearchCategoryMaster' and url='/wcms/masters/categorytype/_search' ;

update eg_action set enabled=true ,name='Update Catgeory Type' where name='ModifyCategoryMasterApi' and url='/wcms/masters/categorytype/{code}/_update';

