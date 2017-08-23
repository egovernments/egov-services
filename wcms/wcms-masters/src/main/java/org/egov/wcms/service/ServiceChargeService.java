package org.egov.wcms.service;

import java.util.List;

import org.egov.wcms.model.ServiceCharge;
import org.egov.wcms.repository.ServiceChargeRepository;
import org.egov.wcms.web.contract.ServiceChargeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceChargeService {
    
    @Autowired
    private ServiceChargeRepository serviceChargeRepository;
    
    public static final Logger logger = LoggerFactory.getLogger(ServiceChargeService.class);

    public List<ServiceCharge> pushServiceChargeCreateRequestToQueue(ServiceChargeReq serviceChargeRequest) {
        logger.info("ServiceChargeReq :" +serviceChargeRequest);
       return serviceChargeRepository.pushServiceChargeCreateReqToQueue(serviceChargeRequest);
        
    }

    public ServiceChargeReq createServiceCharge(ServiceChargeReq serviceChargeRequest) {
       return serviceChargeRepository.persistCreateServiceChargeToDb(serviceChargeRequest);
        
    }

    public List<ServiceCharge> pushServiceChargeUpdateRequestToQueue(ServiceChargeReq serviceChargeRequest) {
        logger.info("ServiceChargeReq :" +serviceChargeRequest);
        return serviceChargeRepository.pushServiceChargeUpdateReqToQueue(serviceChargeRequest);
    }

    public ServiceChargeReq updateServiceCharge(ServiceChargeReq serviceChargeRequest) {
        return serviceChargeRepository.persistUpdateServiceChargeRequestToDB(serviceChargeRequest);
    }

}
