ALTER TABLE egswm_route DROP COLUMN startingCollectionPoint RESTRICT;

ALTER TABLE egswm_route DROP COLUMN endingCollectionPoint RESTRICT;

ALTER TABLE egswm_route DROP COLUMN endingDumpingGroundPoint RESTRICT;

ALTER TABLE egswm_route DROP COLUMN distance RESTRICT;

ALTER TABLE egswm_route DROP COLUMN garbageEstimate RESTRICT;



ALTER TABLE egswm_routecollectionpointmap ADD COLUMN id varchar(256);

ALTER TABLE egswm_routecollectionpointmap ADD COLUMN isStartingCollectionPoint boolean;

ALTER TABLE egswm_routecollectionpointmap ADD COLUMN isEndingCollectionPoint boolean;

ALTER TABLE egswm_routecollectionpointmap ADD COLUMN endingDumpingGroundPoint varchar(256);

ALTER TABLE egswm_routecollectionpointmap ADD COLUMN distance numeric;

ALTER TABLE egswm_routecollectionpointmap ADD COLUMN garbageEstimate numeric;
