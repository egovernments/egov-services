package org.egov.access.domain.service;

import org.egov.access.domain.model.Role;
import org.egov.access.domain.model.RoleSearchCriteria;
import org.egov.access.persistence.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleService {

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles(RoleSearchCriteria roleSearchCriteria) {
       return roleRepository.findForCriteria(roleSearchCriteria);
    }
}
