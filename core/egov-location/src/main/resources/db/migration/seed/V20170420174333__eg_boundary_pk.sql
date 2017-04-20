alter table eg_hierarchy_type drop CONSTRAINT eg_heirarchy_type_pkey cascade;
alter table eg_hierarchy_type drop CONSTRAINT eg_heirarchy_type_type_code_key; 
alter table eg_hierarchy_type drop CONSTRAINT eg_heirarchy_type_type_name_key; 
alter table eg_hierarchy_type add CONSTRAINT eg_hierarchy_type_pkey primary key (id,tenantid);
alter table eg_hierarchy_type add CONSTRAINT eg_hierarchy_type_type_code_key UNIQUE (code,tenantid);
alter table eg_hierarchy_type add CONSTRAINT eg_hierarchy_type_type_name_key UNIQUE (name,tenantid);

alter table eg_boundary_type drop CONSTRAINT eg_boundary_type_pkey cascade;
alter table eg_boundary_type drop CONSTRAINT eg_boundary_type_type_code_key; 
alter table eg_boundary_type drop CONSTRAINT eg_boundary_type_type_name_key; 
alter table eg_boundary_type add CONSTRAINT eg_boundary_type_pkey primary key (id,tenantid);
alter table eg_boundary_type add CONSTRAINT eg_boundary_type_type_code_key UNIQUE (code,tenantid);
alter table eg_boundary_type add CONSTRAINT eg_boundary_type_type_name_key UNIQUE (name,tenantid);


alter table eg_boundary drop CONSTRAINT eg_boundary_pkey cascade;
alter table eg_boundary drop CONSTRAINT eg_boundary_code_key; 
alter table eg_boundary drop CONSTRAINT eg_boundary_name_key; 
alter table eg_boundary add CONSTRAINT eg_boundary_pkey primary key (id,tenantid);
alter table eg_boundary add CONSTRAINT eg_boundary_code_key UNIQUE (code,tenantid);
alter table eg_boundary add CONSTRAINT eg_boundary_name_key UNIQUE (name,tenantid);


ALTER TABLE eg_crosshierarchy drop CONSTRAINT eg_crosshierarchy_pkey cascade;
alter table eg_crosshierarchy drop constraint fk_crossheirarchy_parenttype; 
alter table eg_crosshierarchy drop constraint fk_crossheirarchy_childtype; 
alter table eg_crosshierarchy drop constraint fk_crossheirarchy_parent; 
alter table eg_crosshierarchy drop constraint fk_crossheirarchy_child ;