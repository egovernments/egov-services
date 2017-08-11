update eg_action set url='/wcms/masters/categorytype/_update' where 
name='Update Catgeory Type' and url='/wcms/masters/categorytype/{code}/_update';

update eg_action set url='/wcms/masters/pipesize/_update' where name='ModifyPipeSizeMasterApi' and url='/wcms/masters/pipesize/{code}/_update' ;

update eg_action set url='/wcms/masters/sourcetype/_update' where name='ModifySourceTypeMaster' and url='/wcms/masters/sourcetype/{code}/_update' ;

update eg_action set url='/wcms/masters/supplytype/_update' where name='ModifySupplyTypeMaster' and url='/wcms/masters/supplytype/{code}/_update';

update eg_action set url='/wcms/masters/documenttype/_update' where name='DocumentTypeModify' and url='/wcms/masters/documenttype/{code}/_update';

update eg_action set url='/wcms/masters/documenttype-applicationtype/_update' where name='ModifyDocumentApplicationApi' and url='/wcms/masters/documenttype-applicationtype/{docTypeAppliTypeId}/_update';

update eg_action set url='/wcms/masters/donation/_update' where name='ModifyDonationApi' and url='/wcms/masters/donation/{donationId}/_update';

update eg_action set  url='/wcms/masters/propertytype-pipesize/_update' where name='ModifyPropertyPipeSizeApi' and url='/wcms/masters/propertytype-pipesize/{propertyPipeSizeId}/_update';

update eg_action set url='/wcms/masters/propertytype-categorytype/_update' where name='ModifyPropertyCategoryApi' and url='/wcms/masters/propertytype-categorytype/{propertyCategoryId}/_update';

update eg_action set url='/wcms/masters/propertytype-usagetype/_update' where name='ModifyPropertyUsageApi' and url='/wcms/masters/propertytype-usagetype/{propertyUsageId}/_update';