package org.egov.pgrrest.common.repository;

import org.egov.pgrrest.common.entity.Submission;
import org.egov.pgrrest.common.entity.SubmissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionJpaRepository extends JpaRepository<Submission, SubmissionKey>,
    JpaSpecificationExecutor<Submission> {
}
