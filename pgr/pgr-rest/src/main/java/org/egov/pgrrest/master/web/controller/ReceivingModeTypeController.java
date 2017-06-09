package org.egov.pgrrest.master.web.controller;

import org.egov.pgrrest.master.service.ReceivingModeTypeService;
import org.egov.pgrrest.master.web.contract.factory.ResponseInfoFactory;
import org.egov.pgrrest.master.web.errorhandlers.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receivingmode")
public class ReceivingModeTypeController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ReceivingModeTypeController.class);

    @Autowired
    private ReceivingModeTypeService modeTypeService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

}
