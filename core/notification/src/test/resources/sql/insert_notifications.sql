INSERT INTO notification (id, messagecode, messagevalues, reference, userid, read, createddate, createdby)
VALUES (nextval('seq_notification'), 'message.id', 'key:value', 'crn', 1, 'N', now(), 0);