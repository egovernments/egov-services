----role action mapping

---property-pipesize

delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'CreatPropertyPipeSizeApi') ;
    delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'ModifyPropertyPipeSizeApi') ;
    delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'SearchPropertyPipeSizeApi') ;

delete from eg_action where servicecode='PROPERTYPIPESIZE' ;  
delete from service where code='PROPERTYPIPESIZE';


---property-category

delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'CreatPropertyCategoryApi') ;
    delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'ModifyPropertyCategoryApi') ;
    delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'SearchPropertyCategoryApi') ;

delete from eg_action where servicecode='PROPERTYCATEGORY' ;  
delete from service where code='PROPERTYCATEGORY';

----property-usage

delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'CreatPropertyUsageApi') ;
    delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'ModifyPropertyUsageApi') ;
    delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'SearchPropertyUsageApi') ;

delete from eg_action where servicecode='PROPERTYUSAGE' ;  
delete from service where code='PROPERTYUSAGE';
