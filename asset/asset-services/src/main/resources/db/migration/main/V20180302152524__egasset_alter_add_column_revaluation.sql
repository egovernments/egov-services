--alter add columns

alter table egasset_revalution  add column revaluationorderno character varying(20);
update egasset_revalution set revaluationorderno ='12';
ALTER TABLE egasset_revalution  ALTER COLUMN revaluationorderno SET NOT NULL ;

alter table egasset_revalution  add column revaluationorderdate bigint;
update egasset_revalution  set revaluationorderdate =1504549800000;
ALTER TABLE egasset_revalution  ALTER COLUMN revaluationorderdate SET NOT NULL ;

----drop scheme and subscheme
alter table egasset_revalution drop column scheme ;
alter table egasset_revalution  drop column subscheme;

