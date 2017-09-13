
--for batchsize
INSERT INTO public.egmr_serviceconfiguration(id, keyname, description, createdby, createdtime, lastmodifiedby,lastmodifiedtime, tenantid)
VALUES ((select nextval('seq_egmr_serviceconfiguration')),'MarriageBatchSize', 'Marriage Batch Size','1', extract(epoch from now()) * 1000,'1', extract(epoch from now()) * 1000, 'default');

INSERT INTO public.egmr_serviceconfigurationvalues(id, keyid, value, createdby, createdtime, lastmodifiedby, lastmodifiedtime,tenantid, effectivefrom)
VALUES ((select nextval('seq_egmr_serviceconfigurationvalues')),(select id from egmr_serviceconfiguration where keyname = 'MarriageBatchSize' and tenantid = 'default'), '500', '1', extract(epoch from now()) * 1000, '1', extract(epoch from now()) * 1000,'default', extract(epoch from now()) * 1000);

 --for witness      
INSERT INTO public.egmr_serviceconfiguration(id, keyname, description, createdby, createdtime, lastmodifiedby,lastmodifiedtime, tenantid)
VALUES ((select nextval('seq_egmr_serviceconfiguration')),'MarriageWittnessSize', ' Number of wittnessses for Marriage ','1', extract(epoch from now()) * 1000,'1',extract(epoch from now()) * 1000, 'default');


INSERT INTO public.egmr_serviceconfigurationvalues(id, keyid, value, createdby, createdtime, lastmodifiedby, lastmodifiedtime, tenantid, effectivefrom)
VALUES ((select nextval('seq_egmr_serviceconfigurationvalues')),(select id from egmr_serviceconfiguration where keyname = 'MarriageWittnessSize' and tenantid = 'default'), '4', '1', extract(epoch from now()) * 1000, '1', extract(epoch from now()) * 1000,'default', extract(epoch from now()) * 1000);