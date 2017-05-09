DELETE  FROM data_sync_epoch;

INSERT INTO data_sync_epoch (epoch) SELECT now() AT TIME ZONE 'Asia/Kolkata';