#!/bin/sh

flyway -url=$DB_URL -table=$SCHEMA_TABLE -user=$FLYWAY_USER -password=$FLYWAY_PASSWORD -locations=filesystem:/flyway/sql -baselineOnMigrate=true migrate

if [[ -n "$RUN_FLYWAY_SEED" ]]; then
    flyway -url=$DB_URL -table=$SCHEMA_TABLE -user=$FLYWAY_USER -password=$FLYWAY_PASSWORD -locations=filesystem:/flyway/seed -baselineOnMigrate=true migrate
fi