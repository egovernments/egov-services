DELETE  FROM data_sync_epoch;

INSERT INTO data_sync_epoch (epoch, profile) VALUES (now(), 'ml-to-ms');
INSERT INTO data_sync_epoch (epoch, profile) VALUES (now(), 'ms-to-ml');