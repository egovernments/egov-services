

delete from service_status where code ='CLOSED'  and tenantId='default';

delete from keyword_service_status where servicestatuscode ='CLOSED' and tenantId='default' and keyword='Complaint';

