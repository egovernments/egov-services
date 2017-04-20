package org.egov.pgr.read.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.common.entity.ReceivingCenter;
import org.egov.pgr.read.domain.service.ReceivingCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receivingcenter")
public class ReceivingCenterController {

    @Autowired
    private ReceivingCenterService receivingCenterService;

    @GetMapping
    public List<ReceivingCenter> getAllReceivingCenters(
            @RequestParam(value = "tenantId", required = true) String tenantId) {
        if (tenantId != null && !tenantId.isEmpty())
            return receivingCenterService.getAllReceivingCenters(tenantId);
        else
            return new ArrayList<ReceivingCenter>();
    }

    @PostMapping("/_getreceivingcenterbyid")
    public ReceivingCenter getReceivingCenterById(@RequestParam(value = "tenantId", required = true) String tenantId,
            @RequestParam Long id) {
        if (tenantId != null && !tenantId.isEmpty())
            return receivingCenterService.getReceivingCenterById(tenantId, id);
        else
            return new ReceivingCenter();
    }

}
