DELETE FROM message where code LIKE 'pgr.complaint.category%' AND (tenantid LIKE 'pb%');


insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.NoStreetlight','No streetlight','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.StreetLightNotWorking','Streetlight not working','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.GarbageNeedsTobeCleared','Garbage needs to be cleared','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.Replace/ProvideGarbageBin','Replace/provide garbage bin ','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.BurningOfGarbage','Burning of garbage','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.Overflowing/BlockedDrain','Overflowing/Blocked drain','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.illegalDischargeOfSewage','Illegal discharge of sewage','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.Block/OverflowingSewage','Block / Overflowing sewage','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.ShortageOfWater','Shortage of water','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.DirtyWaterSupply','Dirty water supply','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.BrokenWaterPipeOrLeakage','Broken water pipe / Leakage ','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.WaterPressureisVeryLess','Water pressure is very less','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.StrayAnimals','Stray animals','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.DeadAnimals','Dead animals','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.RequestSprayingOrFoggingOperation','Request spraying/ fogging operations','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.IllegalShopsOnFootPath','Illegal shops on footpath','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.IllegalConstructions','Illegal constructions','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.IllegalParking','Illegal parking','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.IllegalCuttingOfTrees','Illegal Cutting of trees','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.CuttingOrTrimmingOfTreeRequired','Cutting/trimming of tree required','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.DirtyOrSmellyPublicToilets','Dirty/smelly public toilet','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.PublicToiletIsDamaged','Public toilet damaged','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.NoWaterOrElectricityinPublicToilet','No water/electricity in public toilet','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.OpenDefecation','Open Defecation','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.Parks','Park requires maintenance','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.DamagedRoad','Damaged road','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.WaterLoggedRoad','Water logged road','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.ManholeCoverMissingOrDamaged','Manhole cover is missing/damaged','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.Damaged/BlockedFootpath','Damaged/blocked footpath','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.ConstructionMaterialLyingOntheRoad','Construction material lying on the road ','pb','rainmaker-pgr', 1);

insert into message (id,locale,code,message,tenantid,module,createdby)
values(nextval('SEQ_MESSAGE'),'en_IN','pgr.complaint.category.Others','Others','pb','rainmaker-pgr', 1);