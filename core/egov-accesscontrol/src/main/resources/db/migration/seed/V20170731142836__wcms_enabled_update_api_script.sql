---category type

update eg_action set displayname='View Category Type' where name='View Category Type' and url='/wcms/masters/categorytype/_search' ;

update eg_action set displayname='Update Catgeory Type' where name='Update Catgeory Type' and url='/wcms/masters/categorytype/{code}/_update';

--pipesize

update eg_action set enabled=true,displayname='Update Pipe Size' where name='ModifyPipeSizeMasterApi' and url='/wcms/masters/pipesize/{code}/_update' ;

update eg_action set displayname='View Pipe Size' where name='SearchPipeSizeMaster' and url='/wcms/masters/pipesize/_search';

--source type

update eg_action set enabled=true,displayname='Update Water Source Type' where name='ModifySourceTypeMaster' and url='/wcms/masters/sourcetype/{code}/_update' ;

update eg_action set displayname='View Water Source Type' where name='SearchWaterSourceTypeMaster' and url='/wcms/masters/sourcetype/_search';

---supply type

update eg_action set enabled=true,displayname='Update Water Supply Type' where name='ModifySupplyTypeMaster' and url='/wcms/masters/supplytype/{code}/_update';

update eg_action set displayname='View Water Supply Type' where name='SearchWaterSupplyTypeMaster' and url='/wcms/masters/supplytype/_search';

----documenttype

update eg_action set enabled=true,displayname='Update Document Type' where name='DocumentTypeModify' and url='/wcms/masters/documenttype/{code}/_update';

update eg_action set displayname='View Document Type' where name='SearchDocumentTypeMaster' and url='/wcms/masters/documenttype/_search';

-----DocumentTypeApplicationType

update eg_action set enabled=true,displayname='Update Document Application' where name='ModifyDocumentApplicationApi' and url='/wcms/masters/documenttype-applicationtype/{docTypeAppliTypeId}/_update';

update eg_action set displayname='View Document Application' where name='SearchDocumentApplicationApi' and url='/wcms/masters/documenttype-applicationtype/_search';

---Donation

update eg_action set enabled=true,displayname='Update Donation' where name='ModifyDonationApi' and url='/wcms/masters/donation/{donationId}/_update';

update eg_action set displayname='View Donation' where name='SearchDonationApi' and url='/wcms/masters/donation/_search';

-----PropertyPipeSize

update eg_action set enabled=true,displayname='Update Property PipeSize' where name='ModifyPropertyPipeSizeApi' and url='/wcms/masters/propertytype-pipesize/{propertyPipeSizeId}/_update';

update eg_action set displayname='View Property PipeSize' where name='SearchPropertyPipeSizeApi' and url='/wcms/masters/propertytype-pipesize/_search';

----PropertyCategory

update eg_action set enabled=true,displayname='Update Property Category' where name='ModifyPropertyCategoryApi' and url='/wcms/masters/propertytype-categorytype/{propertyCategoryId}/_update';

update eg_action set displayname='View Property Category' where name='SearchPropertyCategoryApi' and url='/wcms/masters/propertytype-categorytype/_search';

----PropertyUsage

update eg_action set displayname='View Property Usage' where name='SearchPropertyUsageApi' and url='/wcms/masters/propertytype-usagetype/_search';

update eg_action set enabled=true,displayname='Update Property Usage' where name='ModifyPropertyUsageApi' and url='/wcms/masters/propertytype-usagetype/{propertyUsageId}/_update';

---StorageReservoir

update eg_action set displayname='View Storage Reservoir' where name='SearchStorageReservoirApi' and url='/wcms/masters/storagereservoir/_search';

update eg_action set enabled=true,displayname='Update Storage Reservoir' where name='ModifyStorageReservoirApi' and url='/wcms/masters/storagereservoir/_update';


---TreatmentPlant

update eg_action set displayname='View Treatment Plant' where name='SearchTreatmentPlantApi' and url='/wcms/masters/treatmentplant/_search';

update eg_action set enabled=true,displayname='Update Treatment Plant' where name='ModifyTreatmentPlantApi' and url='/wcms/masters/treatmentplant/_update';

---Meter Water Rates 

update eg_action set enabled=true,displayname='Update Meter Water Rates' where name='ModifyMeterWaterRatesApi' and url='/wcms/masters/meterwaterrates/_update';

update eg_action set displayname='View Meter Water Rates' where name='SearchMeterWaterRatesApi' and url='/wcms/masters/meterwaterrates/_search';
