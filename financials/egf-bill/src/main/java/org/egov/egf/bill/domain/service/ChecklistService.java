package org.egov.egf.bill.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.domain.repository.ChecklistRepository;
import org.egov.egf.bill.web.requests.ChecklistRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChecklistService {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Transactional
    public ChecklistRequest create(ChecklistRequest checklistRequest) {

        fetchRelated(checklistRequest.getChecklists());
        validate(checklistRequest);

        for (final Checklist cl : checklistRequest.getChecklists())
            cl.setCode(UUID.randomUUID().toString().replace("-", ""));

        return checklistRepository.save(checklistRequest);

    }

    @Transactional
    public ChecklistRequest update(ChecklistRequest checklistRequest) {

        fetchRelated(checklistRequest.getChecklists());
        validate(checklistRequest);

        return checklistRepository.update(checklistRequest);

    }

    private void validate(ChecklistRequest checklistRequest) {
        findDuplicatesInUniqueFields(checklistRequest);
        for (Checklist cl : checklistRequest.getChecklists()) {
            validateUniqueFields(cl);
        }

    }

    private void findDuplicatesInUniqueFields(ChecklistRequest checklistRequest) {

        final Map<String, String> typeMap = new HashMap<>();
        final Map<String, String> subTypeMap = new HashMap<>();

        for (final Checklist cl : checklistRequest.getChecklists()) {
            if (cl.getType() != null) {

                if (typeMap.get(cl.getType()) != null)
                    throw new CustomException("Type",
                            "Duplicate types in given Checklists: " + cl.getType());

                typeMap.put(cl.getType(), cl.getType());

            }

            if (cl.getSubType() != null) {

                if (subTypeMap.get(cl.getSubType()) != null)
                    throw new CustomException("SubType",
                            "Duplicate subtypes in given Checklists: " + cl.getSubType());

                subTypeMap.put(cl.getSubType(), cl.getSubType());

            }

        }

    }

    private void validateUniqueFields(final Checklist checllist) {

        if (checllist.getType() != null)
            if (!checklistRepository.uniqueCheck(checllist.getTenantId(), "type", checllist.getType(),
                    "code", checllist.getCode()))
                throw new CustomException("Type",
                        "The field type must be unique in the system The  value " + checllist.getType()
                                + " for the field type already exists in the system. Please provide different value ");

        if (checllist.getSubType() != null)
            if (!checklistRepository.uniqueCheck(checllist.getTenantId(), "subType", checllist.getSubType(),
                    "code", checllist.getCode()))
                throw new CustomException("subType",
                        "The field subType must be unique in the system The  value " + checllist.getSubType()
                                + " for the field subType already exists in the system. Please provide different value ");

    }

    public List<Checklist> fetchRelated(final List<Checklist> checklists) {
        return checklists;
    }

    public Pagination<Checklist> search(final ChecklistSearch checklistSearch) {

        return checklistRepository.search(checklistSearch);
    }

}