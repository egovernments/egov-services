DELETE from message where code in ('pgr.complaint.category.Replace/ProvideGarbageBin', 'pgr.complaint.category.Overflowing/BlockedDrain',
'pgr.complaint.category.Block/OverflowingSewage', 'pgr.complaint.category.Damaged/BlockedFootpath');

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.ReplaceOrProvideGarbageBin','Replace or provide garbage bin ','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.OverflowingOrBlockedDrain','Overflowing or Blocked drain','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.BlockOrOverflowingSewage','Block or Overflowing sewage','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.DamagedOrBlockedFootpath','Damaged or blocked footpath','pb','rainmaker-pgr', 1);