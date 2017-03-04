package org.egov.persistence.repository;

import org.egov.persistence.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {
}
