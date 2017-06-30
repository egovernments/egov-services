update eg_action set url='/pgr/seva/v1/_create' where url='/pgr/seva/_create' and name='Create Complaint' and parentmodule ='PGR';
update eg_action set url='/pgr/seva/v1/_update' where url='/pgr/seva/_update' and name='Update Complaint' and parentmodule ='PGR';
update eg_action set url='/pgr/seva/v1/_search' where url='/pgr/seva/_search' and name='Search Complaint' and parentmodule ='PGR';

update eg_action set url='/pgr/receivingmode/v1/_search' where url='/pgr/receivingmode/_search' and name='Get all ReceivingMode' and parentmodule ='PGR';
update eg_action set url='/pgr/receivingcenter/v1/_search' where url='/pgr/receivingcenter/_search' and name='Get all ReceivingCenters' and parentmodule ='PGR';

update eg_action set url='/pgr/services/v1/_search' where url='/pgr/services/_search' and name='Get ComplaintType by type,count and tenantId' and parentmodule ='PGR';
update eg_action set url='/pgr/services/v1/_search' where url='/pgr/services/_search' and name='Get ComplaintType by type,categoryId and tenantId' and parentmodule ='PGR';
update eg_action set url='/pgr/services/v1/_search' where url='/pgr/services/_search' and name='Get ComplaintType by type and tenantId' and parentmodule ='PGR';

update eg_action set url='/pgr/servicecategories/v1/_search' where url='/pgr/servicecategories/_search' and name ='Get all service type categories' and parentmodule ='PGR';

update eg_action set url='/pgr/statuses/v1/_search' where url='/pgr/_statuses' and name='Get all Statuses' and parentmodule ='PGR';
update eg_action set url='/pgr/nextstatuses/v1/_search' where url='/pgr/_getnextstatuses' and name='Get next statuses by CurrentStatus and Role' and parentmodule ='PGR';
update eg_action set url='/workflow/history/v1/_search' where url='/workflow/history' and name='Get Workflow History' and parentmodule ='PGR';