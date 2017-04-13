#!/bin/sh

flyway -url=$DB_URL -table=$SCHEMA_TABLE -user=$FLYWAY_USER -password=$FLYWAY_PASSWORD -locations=filesystem:/flyway/sql -baselineOnMigrate=true -outOfOrder=true migrate

if [[ "$RUN_FLYWAY_SEED" == "true" ]]; then
    flyway -url=$DB_URL -table=$SCHEMA_TABLE -user=$FLYWAY_USER -password=$FLYWAY_PASSWORD -locations=filesystem:/flyway/seed -baselineOnMigrate=true -outOfOrder=true migrate
fi