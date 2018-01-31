package org.egov.works.estimate.domain.service;

import net.minidev.json.JSONArray;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.ProjectCodeRepository;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class ProjectCodeService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private ProjectCodeRepository projectCodeRepository;

    @Autowired
    private IdGenerationRepository idGenerationRepository;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private EstimateUtils estimateUtils;

    public List<ProjectCode> create(ProjectCodeRequest projectCodeRequest) {

        String workIdentificationNumber;
        for (ProjectCode projectCode : projectCodeRequest.getProjectCodes()) {
            if (projectCode.getCode() != null && !projectCode.getCode().isEmpty())
                validateProjectCode(projectCode);
            projectCode.setId(commonUtils.getUUID());

            projectCode.setAuditDetails(estimateUtils.setAuditDetails(projectCodeRequest.getRequestInfo(), false));
            projectCode.setActive(true);
            List<String> filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE, CommonConstants.MODULE_TYPE));
            List<String> filetsValuesList = new ArrayList<>(Arrays.asList(CommonConstants.STATUS_CREATED,Constants.PROJECTCODE_OBJECT));
            JSONArray dBStatusArray = estimateUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                    filetsValuesList, projectCode.getTenantId(), projectCodeRequest.getRequestInfo(),
                    CommonConstants.MODULENAME_WORKS);
            if(dBStatusArray != null && !dBStatusArray.isEmpty()) {
                WorksStatus status = new WorksStatus();
                status.code(CommonConstants.STATUS_CREATED);
                projectCode.setStatus(status);
            }

            if (projectCode.getCode() == null || projectCode.getCode().isEmpty()) {
                workIdentificationNumber = idGenerationRepository.generateWorkIdentificationNumber(
                        projectCode.getTenantId(), projectCodeRequest.getRequestInfo());

                projectCode.setCode(
                        estimateUtils.getCityCode(projectCode.getTenantId(), projectCodeRequest.getRequestInfo()) + "/" + propertiesManager.getWorkIdentificationNumberPrefix() + "/" + workIdentificationNumber);
            }

        }

        kafkaTemplate.send(propertiesManager.getWorksProjectCodeCreateTopic(), projectCodeRequest);
        // TODO: To be enabled when account detail consumer is available
        // kafkaTemplate.send(propertiesManager.getCreateAccountDetailKeyTopic(),
        // projectCodeRequest);
        return projectCodeRequest.getProjectCodes();
    }

    private void validateProjectCode(ProjectCode projectCode) {
        Map<String, String> messages = new HashMap<>();
        ProjectCodeSearchContract projectCodeSearchContract = new ProjectCodeSearchContract();
        List<String> workIdentificationNumbers = new ArrayList<>();
        workIdentificationNumbers.add(projectCode.getCode());
        projectCodeSearchContract.setWorkIdentificationNumbers(workIdentificationNumbers);

        List<ProjectCode> projectCodes = search(projectCodeSearchContract);
        if (!projectCodes.isEmpty()) {
            messages.put(Constants.KEY_UNIQUE_WORKIDENTIFICATIONNUMBER,
                    Constants.MESSAGE_UNIQUE_WORKIDENTIFICATIONNUMBER);
            throw new CustomException(messages);
        }
    }

    public List<ProjectCode> search(ProjectCodeSearchContract projectCodeSearchContract) {
        return projectCodeRepository.search(projectCodeSearchContract);
    }

    public List<ProjectCode> update(ProjectCodeRequest projectCodeRequest) {

        for (ProjectCode projectCode : projectCodeRequest.getProjectCodes()) {
            projectCode.setAuditDetails(estimateUtils.setAuditDetails(projectCodeRequest.getRequestInfo(), true));
        }
        kafkaTemplate.send(propertiesManager.getWorksProjectCodeUpdateTopic(), projectCodeRequest);
        return projectCodeRequest.getProjectCodes();
    }

}
