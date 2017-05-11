DELETE  FROM data_sync_epoch;

UPDATE data_sync_epoch set epoch = '2017-05-01 00:00:00' where profile = 'ml-to-ms';
UPDATE data_sync_epoch set epoch = '2017-05-01 00:00:00' where profile = 'ms-to-ml';