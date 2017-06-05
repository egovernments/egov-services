delete from value_definition where servicecode='PPTNDC' and key='LPP' and tenantid='panavel';
delete from value_definition where servicecode='PPTNDC' and key='HYP' and tenantid='panavel';
delete from value_definition where servicecode='PPTNDC' and key='OP' and tenantid='panavel';

delete from attribute_definition where servicecode='PPTNDC' and code='PPID' and tenantid='panavel';
delete from attribute_definition where servicecode='PPTNDC' and code='DOCUMENTS' and tenantid='panavel';
delete from attribute_definition where servicecode='PPTNDC' and code='CHECKLIST' and tenantid='panavel';
delete from attribute_definition where servicecode='PPTNDC' and code='APPLICATIONFEE' and tenantid='panavel';

delete from service_definition where code='PPTNDC' and tenantid='panavel';

----------------------------------------------------------------------------------------------------------------
delete from value_definition where servicecode='PPTAC' and key='LPP' and tenantid='panavel';
delete from value_definition where servicecode='PPTAC' and key='HYP' and tenantid='panavel';
delete from value_definition where servicecode='PPTAC' and key='OP' and tenantid='panavel';

delete from attribute_definition where servicecode='PPTAC' and code='PTID' and tenantid='panavel';
delete from attribute_definition where servicecode='PPTAC' and code='DOCUMENTS' and tenantid='panavel';
delete from attribute_definition where servicecode='PPTAC' and code='CHECKLIST' and tenantid='panavel';
delete from attribute_definition where servicecode='PPTAC' and code='APPLICATIONFEE' and tenantid='panavel';

delete from service_definition where code='PPTAC' and tenantid='panavel';


