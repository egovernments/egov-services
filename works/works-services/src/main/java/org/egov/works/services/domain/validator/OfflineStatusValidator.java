package org.egov.works.services.domain.validator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.web.contract.DetailedEstimateOfflineStatus;
import org.egov.works.commons.web.contract.LOAOfflineStatuses;
import org.egov.works.commons.web.contract.WorkOrderOfflineStatus;
import org.egov.works.services.domain.service.OfflineStatusService;
import org.egov.works.services.web.contract.OfflineStatus;
import org.egov.works.services.web.contract.OfflineStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OfflineStatusValidator {
    

	@Autowired
	private OfflineStatusService offlineStatusService;
	
    public void validate(OfflineStatusRequest offlineStatusRequest) {
    	
		String[] statusName;
		List<String> OffStatuses;
		final String[] selectedStatusArr = new String[offlineStatusRequest.getOfflineStatuses().size()];;
		int b = 0;
		Map<String, String> messages = new HashMap<>();

		final String ObjectType = offlineStatusRequest.getOfflineStatuses().get(0).getObjectType();
		OffStatuses = getListOfStatusForObjectType(ObjectType);

		for (int i = 0; i < offlineStatusRequest.getOfflineStatuses().size(); i++) {
			selectedStatusArr[i] = offlineStatusRequest.getOfflineStatuses().get(i).getStatus();
		}

		for (final String statName : offlineStatusService.getStatusNameDetails(selectedStatusArr)) {
			if (!OffStatuses.isEmpty() && !statName.equals(OffStatuses.get(b))) {
				
				String s = statName + " should be set After status " + OffStatuses.get(OffStatuses.indexOf(statName) - 1);
				
				messages.put("Please Send Proper order", s);
				break;
			}
			b++;
		}

		final List<OfflineStatus> newOfflineStatus = offlineStatusRequest.getOfflineStatuses();
		for (int a = 0; a < newOfflineStatus.size() - 1; a++)
			if (newOfflineStatus.get(a).getStatusDate() > newOfflineStatus.get(a + 1).getStatusDate()) {
				messages.put("Incorrect Date for Order", "Status Date for " + newOfflineStatus.get(a+1).getStatus()
						+ " should be greater then" + newOfflineStatus.get(a).getStatus());
				break;
			}

		if (messages != null && !messages.isEmpty())
			throw new CustomException(messages);
		
    }

	private List<String> getListOfStatusForObjectType(final String ObjectType) {
		
		String[] statusName;
		List<String> OffStatuses;
		if(ObjectType.equalsIgnoreCase(CommonConstants.WORKORDER)) {
		final WorkOrderOfflineStatus[] workOrderOfflineStatus = WorkOrderOfflineStatus.values();
		statusName = new String[workOrderOfflineStatus.length];
		for (int j = 0; j < workOrderOfflineStatus.length; j++)
			statusName[j] = workOrderOfflineStatus[j].name();
		OffStatuses = Arrays.asList(statusName);
		} else if(ObjectType.equalsIgnoreCase(CommonConstants.LOA)){
			final LOAOfflineStatuses[] loaOfflineStatuses = LOAOfflineStatuses.values();
			statusName = new String[loaOfflineStatuses.length];
			for (int j = 0; j < loaOfflineStatuses.length; j++)
				statusName[j] = loaOfflineStatuses[j].name();
			OffStatuses = Arrays.asList(statusName);
		} else {
			final DetailedEstimateOfflineStatus[] detailedEstimateOfflineStatus = DetailedEstimateOfflineStatus.values();
			statusName = new String[detailedEstimateOfflineStatus.length];
			for (int j = 0; j < detailedEstimateOfflineStatus.length; j++)
				statusName[j] = detailedEstimateOfflineStatus[j].name();
			OffStatuses = Arrays.asList(statusName);
		}
		
		return OffStatuses;
	}

}
