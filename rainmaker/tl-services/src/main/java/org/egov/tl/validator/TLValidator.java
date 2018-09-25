package org.egov.tl.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tl.repository.TLRepository;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.egov.tl.web.models.TradeLicenseSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class TLValidator {


    private TLRepository tlRepository;

    @Autowired
    public TLValidator(TLRepository tlRepository) {
        this.tlRepository = tlRepository;
    }




    public void validateUpdate(TradeLicenseRequest request){
      RequestInfo requestInfo = request.getRequestInfo();
      List<TradeLicense> licenses = request.getLicenses();

      Map<String,List<String>> tenantIdToIds= new HashMap<>();
      request.getLicenses().forEach(license -> {
          if(tenantIdToIds.containsKey(license.getTenantId()))
              tenantIdToIds.get(license.getTenantId()).add(license.getId());
          else {
              List<String> ids = new LinkedList<>();
              ids.add(license.getId());
              tenantIdToIds.put(license.getTenantId(),ids);
          }
      });
      List<TradeLicense> licensesFromSearch = new LinkedList<>();
      tenantIdToIds.keySet().forEach(key -> {
          TradeLicenseSearchCriteria criteria = new TradeLicenseSearchCriteria();
          criteria.setTenantId(key);
          criteria.setIds(tenantIdToIds.get(key));
          licensesFromSearch.addAll(tlRepository.getLicenses(criteria));
      });

      if(licensesFromSearch.size()!=licenses.size())
          throw new CustomException("INVALID UPDATE","The license to be updated is not in database");

  }


}


