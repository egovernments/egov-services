#!/bin/sh

envtpl < config.yaml.tmpl > config.yaml
for ruletemplate in ./rules/*.yaml.tmpl
do
  echo "Reading $ruletemplate"
  envtpl < $ruletemplate > `echo ${ruletemplate} | sed -e "s/\.tmpl$//"`
done

elastalert-create-index --index "${ELASTALERT_META_INDEX:-elastalert_status}" --old-index "${ELASTALERT_OLD_INDEX:-}" --config ./config.yaml
elastalert --pin_rules --es_debug --verbose
