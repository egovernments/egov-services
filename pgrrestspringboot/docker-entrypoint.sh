#!/usr/bin/env bash

j2 /templates/application.properties.j2 > "$PROJECT_NAME/src/main/resources"

exec "$@"
