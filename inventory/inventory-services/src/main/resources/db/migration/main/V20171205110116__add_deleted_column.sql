alter TABLE supplier add column isdeleted boolean default false;
alter TABLE store add column isdeleted boolean default false; 
alter TABLE purchaseorder add column isdeleted boolean default false; 
alter TABLE pricelist add column isdeleted boolean default false; 
alter TABLE materialstoremapping add column isdeleted boolean default false; 
alter TABLE materialissue add column isdeleted boolean default false; 
alter TABLE indent add column isdeleted boolean default false; 





