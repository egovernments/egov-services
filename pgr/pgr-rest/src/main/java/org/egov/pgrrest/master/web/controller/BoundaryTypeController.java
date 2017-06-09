package org.egov.pgrrest.master.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.egov.pgrrest.master.model.enums.BoundaryType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/master")
public class BoundaryTypeController {

    @RequestMapping(value = "/_getboundarytypes")
    public Map<String, BoundaryType> getApplicationTypeEnum() {
        final Map<String, BoundaryType> boundaryType = new HashMap<>();
        for (final BoundaryType key : BoundaryType.values())
            boundaryType.put(key.name(), key);
        return boundaryType;
    }

}



