package org.egov.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crn")
public class CrnController {

    @GetMapping()
    public String getCrn() {
        return "Hello";
    }
}