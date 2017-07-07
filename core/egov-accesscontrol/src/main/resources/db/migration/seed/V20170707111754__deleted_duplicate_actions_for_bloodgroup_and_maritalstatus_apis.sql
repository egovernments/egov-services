DELETE FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'SearchBloodGroup') AND tenantId = 'default';
DELETE FROM eg_roleaction
    WHERE actionId = (SELECT id FROM eg_action WHERE name = 'SearchMaritalStatus') AND tenantId = 'default';

DELETE FROM eg_action WHERE name = 'SearchBloodGroup';
DELETE FROM eg_action WHERE name = 'SearchMaritalStatus';