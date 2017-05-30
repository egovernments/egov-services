package org.egov.access.domain.service;

import org.egov.access.domain.criteria.RoleSearchCriteria;
import org.egov.access.domain.model.Role;
import org.egov.access.persistence.repository.BaseRepository;
import org.egov.access.persistence.repository.querybuilder.RoleFinderQueryBuilder;
import org.egov.access.persistence.repository.rowmapper.RoleRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleService {

    private BaseRepository repository;

    public RoleService(BaseRepository repository) {
        this.repository = repository;
    }

    public List<Role> getRoles(RoleSearchCriteria roleSearchCriteria) {
        RoleFinderQueryBuilder queryBuilder = new RoleFinderQueryBuilder(roleSearchCriteria);
        return (List<Role>) (List<?>) repository.run(queryBuilder, new RoleRowMapper());
    }
}
