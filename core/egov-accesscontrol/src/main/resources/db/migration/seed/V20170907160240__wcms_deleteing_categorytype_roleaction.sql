-----category type

delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'CreateCategoryMasterApi') ;
    delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'Update Catgeory Type') ;
    delete FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'View Category Type') ;

delete from eg_action where servicecode='CATEGORYMASTERS' ;  
delete from service where code='CATEGORYMASTERS';