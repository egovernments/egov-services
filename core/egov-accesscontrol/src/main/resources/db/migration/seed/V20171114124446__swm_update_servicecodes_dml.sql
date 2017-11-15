update service set code = 'Vehicle Maintenance Details' where code = 'SWMVMD';

update eg_action set servicecode = 'Refillin Pump Station' where name like '%RefillinPumpStation%';

update eg_action set servicecode = 'Vendor Contract' where name like '%VendorContract%';