package org.egov.access.domain.service;

import java.util.List;

import org.egov.access.domain.criteria.RoleSearchCriteria;
import org.egov.access.domain.model.Role;
import org.egov.access.persistence.repository.BaseRepository;
import org.egov.access.persistence.repository.RoleRepository;
import org.egov.access.persistence.repository.querybuilder.RoleFinderQueryBuilder;
import org.egov.access.persistence.repository.rowmapper.RoleRowMapper;
import org.egov.access.web.contract.role.RoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleService {

    private BaseRepository repository;
    
    @Autowired
	private RoleRepository roleRepository;

    public RoleService(BaseRepository repository) {
        this.repository = repository;
    }

    public List<Role> getRoles(RoleSearchCriteria roleSearchCriteria) {
        RoleFinderQueryBuilder queryBuilder = new RoleFinderQueryBuilder(roleSearchCriteria);
        return (List<Role>) (List<?>) repository.run(queryBuilder, new RoleRowMapper());
    }
    
    public List<Role> createRole(RoleRequest roleRequest){
    	
    	return roleRepository.createRole(roleRequest);
    }
    
    public List<Role> updateRole(RoleRequest roleRequest){
    	
    	return roleRepository.updateRole(roleRequest);
    }
    
    public boolean checkRoleNameDuplicationValidationErrors(String roleName){
    	
    	return roleRepository.checkRoleNameDuplicationValidationErrors(roleName);
    }
    
}
