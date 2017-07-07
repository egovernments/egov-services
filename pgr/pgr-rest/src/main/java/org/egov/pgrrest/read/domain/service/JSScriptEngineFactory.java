package org.egov.pgrrest.read.domain.service;

import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Service
public class JSScriptEngineFactory {

    private static final String ENGINE_NAME = "nashorn";

    public ScriptEngine create() {
        return new ScriptEngineManager().getEngineByName(ENGINE_NAME);
    }

}
