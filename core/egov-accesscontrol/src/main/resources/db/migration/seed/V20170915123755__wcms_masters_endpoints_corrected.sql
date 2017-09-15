--pipesizes

update eg_action set url='/wcms/masters/pipesizes/_create' where name='CreatePipeSizeMasterApi' and url='/wcms/masters/pipesize/_create'  ;

update eg_action set url='/wcms/masters/pipesizes/_update' where name='ModifyPipeSizeMasterApi' and url='/wcms/masters/pipesize/_update' ;

update eg_action set url='/wcms/masters/pipesizes/_search' where name='SearchPipeSizeMaster' and url='/wcms/masters/pipesize/_search' ;

--sourceType

update eg_action set url='/wcms/masters/sourcetypes/_create' where name='CreateSourceTypeMasterApi' and url='/wcms/masters/sourcetype/_create' ;

update eg_action set url='/wcms/masters/sourcetypes/_update' where name='ModifySourceTypeMaster' and url='/wcms/masters/sourcetype/_update' ;

update eg_action set url='/wcms/masters/sourcetypes/_search' where name='SearchWaterSourceTypeMaster' and url='/wcms/masters/sourcetype/_search' ;


---supplytype

update eg_action set url='/wcms/masters/supplytypes/_create' where name='CreateSupplyTypeMasterApi' and url='/wcms/masters/supplytype/_create' ;

update eg_action set url='/wcms/masters/supplytypes/_update' where name='ModifySupplyTypeMaster' and url='/wcms/masters/supplytype/_update' ;

update eg_action set url='/wcms/masters/supplytypes/_search' where name='SearchWaterSupplyTypeMaster' and url='/wcms/masters/supplytype/_search' ;

----documenttype

update eg_action set url='/wcms/masters/documenttypes/_create' where name='CreateDocumentTypeMasterApi' and url='/wcms/masters/documenttype/_create' ;

update eg_action set url='/wcms/masters/documenttypes/_update' where name='DocumentTypeModify' and url='/wcms/masters/documenttype/_update' ;

update eg_action set url='/wcms/masters/documenttypes/_search' where name='SearchDocumentTypeMaster' and url='/wcms/masters/documenttype/_search' ;


------donation

update eg_action set url='/wcms/masters/donations/_create' where name='CreatDonationApi' and url='/wcms/masters/donation/_create' ;

update eg_action set url='/wcms/masters/donations/_update' where name='ModifyDonationApi' and url='/wcms/masters/donation/_update' ;

update eg_action set url='/wcms/masters/donations/_search' where name='SearchDonationApi' and url='/wcms/masters/donation/_search' ;


------storage reservoir

update eg_action set url='/wcms/masters/storagereservoirs/_create' where name='CreatStorageReservoirApi' and url='/wcms/masters/storagereservoir/_create' ;

update eg_action set url='/wcms/masters/storagereservoirs/_search' where name='SearchStorageReservoirApi' and url='/wcms/masters/storagereservoir/_search' ;

update eg_action set url='/wcms/masters/storagereservoirs/_update' where name='ModifyStorageReservoirApi' and url='/wcms/masters/storagereservoir/_update' ;


------treatment plant

update eg_action set url='/wcms/masters/treatmentplants/_create' where name='CreatTreatmentPlantApi' and url='/wcms/masters/treatmentplant/_create' ;

update eg_action set url='/wcms/masters/treatmentplants/_search' where name='SearchTreatmentPlantApi' and url='/wcms/masters/treatmentplant/_search' ;

update eg_action set url='/wcms/masters/treatmentplants/_update' where name='ModifyTreatmentPlantApi' and url='/wcms/masters/treatmentplant/_update' ;

------gap code 

update eg_action set url='/wcms/masters/gapcodes/_create' where name='CreateGapcodeMaster' and url='/wcms/masters/gapcode/_create' ;

update eg_action set url='/wcms/masters/gapcodes/_update' where name='UpdateGapcodeMaster' and url='/wcms/masters/gapcode/_update' ;

update eg_action set url='/wcms/masters/gapcodes/_search' where name='SearchGapcodeMaster' and url='/wcms/masters/gapcode/_search' ;

------usage type 

update eg_action set url='/wcms/masters/usagetypes/_create' where name='CreateUsageTypeMaster' and url='/wcms/masters/usagetype/_create' ;

update eg_action set url='/wcms/masters/usagetypes/_update' where name='UpdateUsageTypeMaster' and url='/wcms/masters/usagetype/_update' ;

update eg_action set url='/wcms/masters/usagetypes/_search' where name='SearchUsageTypeMaster' and url='/wcms/masters/usagetype/_search' ;


-----sub usage type 

update eg_action set url='/wcms/masters/usagetypes/_create' where name='CreateSubUsageTypeMaster' and url='/wcms/masters/usagetype/_create' ;

update eg_action set url='/wcms/masters/usagetypes/_update' where name='UpdateSubUsageTypeMaster' and url='/wcms/masters/usagetype/_update' ;

update eg_action set url='/wcms/masters/usagetypes/_search' where name='SearchSubUsageTypeMaster' and url='/wcms/masters/usagetype/_search' ;

-----document -application

update eg_action set url='/wcms/masters/documenttypes-applicationtypes/_create' where name='CreatDocumentApplicationApi' and url='/wcms/masters/documenttype-applicationtype/_create' ;

update eg_action set url='/wcms/masters/documenttypes-applicationtypes/_search' where name='SearchDocumentApplicationApi' and url='/wcms/masters/documenttype-applicationtype/_search' ;

update eg_action set url='/wcms/masters/documenttypes-applicationtypes/_update' where name='ModifyDocumentApplicationApi' and url='/wcms/masters/documenttype-applicationtype/_update' ;

