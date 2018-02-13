alter table egw_letterofacceptance add column workorderexists boolean default false;
alter table egw_letterofacceptance add column withAllOfflineStatusAndWONotCreated boolean default false;
alter table egw_letterofacceptance add column withoutOfflineStatus boolean default false;
alter table egw_letterofacceptance add column milestoneExists boolean default false;
alter table egw_letterofacceptance add column billExists boolean default false;
alter table egw_letterofacceptance add column contractorAdvanceExists boolean default false;
alter table egw_letterofacceptance add column mbExistsAndBillNotCreated boolean default false;

alter table egw_workorder add column withAllOfflineStatusAndWONotCreated boolean default false;
alter table egw_workorder add column withoutOfflineStatus boolean default false;
alter table egw_workorder add column milestoneExists boolean default false;
alter table egw_workorder add column billExists boolean default false;
alter table egw_workorder add column contractorAdvanceExists boolean default false;
alter table egw_workorder add column mbExistsAndBillNotCreated boolean default false;