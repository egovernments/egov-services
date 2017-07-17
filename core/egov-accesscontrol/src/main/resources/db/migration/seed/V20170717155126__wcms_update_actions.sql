---category Type
update eg_action set name='CreateCategoryMasterApi' where name='Creat Category Master' and url='/categorytype/_create'  ;

update eg_action set name='ModifyCategoryMasterApi' where name='Category Modify Master' and url='/categorytype/{code}/_update' ;

---pipesize
update eg_action set name='CreatePipeSizeMasterApi' where name='Creat PipeSize Master' and url='/pipesize/_create'  ;

update eg_action set name='CreatePipeSizeMasterApi' where name='Creat PipeSize Master' and url='/pipesize/_create' ;

--sourceType
update eg_action set name='CreateSourceTypeMasterApi' where name='Creat SourceType Master' and url='/sourcetype/_create' ;


---supplytype

update eg_action set name='CreateSupplyTypeMasterApi' where name='Creat SupplyType Master' and url='/supplytype/_create'  ;

----documenttype

update eg_action set name='CreateDocumentTypeMasterApi' where name='Creat Document Type Master' and url='/documenttype/_create' ;