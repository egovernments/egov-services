package org.egov.pgrrest.common.repository;

import org.egov.pgrrest.common.entity.Submission;
import org.egov.pgrrest.common.entity.SubmissionKey;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionJpaRepository extends PagingAndSortingRepository<Submission, SubmissionKey>,
    JpaSpecificationExecutor<Submission> {
}
