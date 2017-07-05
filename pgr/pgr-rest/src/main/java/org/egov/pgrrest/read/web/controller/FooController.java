package org.egov.pgrrest.read.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@RestController
public class FooController {

    @GetMapping("/foo")
    public String getFoo() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.put("a", "It's working");
        return String.valueOf(engine.get("a"));
    }
}
