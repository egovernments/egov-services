envtpl < config.yaml.tmpl > config.yaml
elastalert-create-index --index "${ELASTALERT_META_INDEX:-elastalert_status}" --old-index "${ELASTALERT_OLD_INDEX:-''}" --config ./config.yaml
elastalert --debug
