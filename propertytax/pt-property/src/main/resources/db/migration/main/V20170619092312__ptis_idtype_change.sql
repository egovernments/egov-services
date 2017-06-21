
ALTER TABLE egpt_mstr_floor RENAME TO egpt_mstr_floortype;
CREATE SEQUENCE seq_egpt_mstr_floortype;
ALTER TABLE egpt_mstr_structure RENAME TO egpt_mstr_structureclass;
CREATE SEQUENCE seq_egpt_mstr_structureclass;
ALTER TABLE egpt_mstr_wall RENAME TO egpt_mstr_walltype;
CREATE SEQUENCE seq_egpt_mstr_walltype;
ALTER TABLE egpt_mstr_roof RENAME TO egpt_mstr_rooftype;
CREATE SEQUENCE seq_egpt_mstr_rooftype;
ALTER TABLE egpt_mstr_wood RENAME TO egpt_mstr_woodtype;
CREATE SEQUENCE seq_egpt_mstr_woodtype;
ALTER TABLE egpt_mstr_property RENAME TO egpt_mstr_propertytype;
CREATE SEQUENCE seq_egpt_mstr_propertytype;



ALTER TABLE egpt_property ALTER COLUMN id SET DEFAULT nextval('seq_egpt_property');
ALTER TABLE egpt_address ALTER COLUMN id SET DEFAULT nextval('seq_egpt_address');
ALTER TABLE egpt_propertydetails ALTER COLUMN id SET DEFAULT nextval('seq_egpt_propertydetails');
ALTER TABLE egpt_floors ALTER COLUMN id SET DEFAULT nextval('seq_egpt_floors');
ALTER TABLE egpt_unit ALTER COLUMN id SET DEFAULT nextval('seq_egpt_unit');
ALTER TABLE egpt_document ALTER COLUMN id SET DEFAULT nextval('seq_egpt_document');
ALTER TABLE egpt_documenttype ALTER COLUMN id SET DEFAULT nextval('seq_egpt_documenttype');
ALTER TABLE egpt_vacantland ALTER COLUMN id SET DEFAULT nextval('seq_egpt_vacantland');
ALTER TABLE egpt_property_owner ALTER COLUMN id SET DEFAULT nextval('seq_egpt_property_owner');
ALTER TABLE egpt_propertylocation ALTER COLUMN id SET DEFAULT nextval('seq_egpt_propertylocation');
ALTER TABLE egpt_mstr_department ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_department');
ALTER TABLE egpt_mstr_floortype ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_floortype');
ALTER TABLE egpt_mstr_occuapancy ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_occuapancy');
ALTER TABLE egpt_mstr_propertytype ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_propertytype');
ALTER TABLE egpt_mstr_rooftype ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_rooftype');
ALTER TABLE egpt_mstr_structureclass ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_structureclass');
ALTER TABLE egpt_mstr_usage ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_usage');
ALTER TABLE egpt_mstr_walltype ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_walltype');
ALTER TABLE egpt_mstr_woodtype ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_woodtype');
