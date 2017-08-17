package org.egov.pgr.domain.service;

import java.util.List;

import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.model.ServiceTypeConfigurationSearchCriteria;
import org.egov.pgr.domain.service.validator.ServiceTypeConfigurationValidator;
import org.egov.pgr.persistence.repository.ServiceTypeConfigurationMessageQueueRepository;
import org.egov.pgr.persistence.repository.ServiceTypeConfigurationRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class ServiceTypeConfigurationService {

	private ServiceTypeConfigurationMessageQueueRepository serviceTypeConfigurationMessageQueueRepository;

	private ServiceTypeConfigurationRepository serviceTypeConfigurationRepository;

	private List<ServiceTypeConfigurationValidator> validators;

	public ServiceTypeConfigurationService(List<ServiceTypeConfigurationValidator> validators,
			ServiceTypeConfigurationMessageQueueRepository serviceTypeConfigurationMessageQueueRepository,
			ServiceTypeConfigurationRepository serviceTypeConfigurationRepository) {
		this.validators = validators;
		this.serviceTypeConfigurationMessageQueueRepository = serviceTypeConfigurationMessageQueueRepository;
		this.serviceTypeConfigurationRepository = serviceTypeConfigurationRepository;
	}

	public void create(ServiceTypeConfiguration serviceTypeConfiguration) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		validate(serviceTypeConfiguration);
		codeValidate(serviceTypeConfiguration);

		serviceTypeConfigurationMessageQueueRepository.save(serviceTypeConfiguration.toDto());

	}

	public void update(ServiceTypeConfiguration serviceTypeConfiguration) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		validate(serviceTypeConfiguration);
		updateValidate(serviceTypeConfiguration);

		serviceTypeConfigurationMessageQueueRepository.saves(serviceTypeConfiguration.toDto());

	}

	public List<ServiceTypeConfiguration> search(ServiceTypeConfigurationSearchCriteria serviceTypeSearchCriteria) {
		validateSearch(serviceTypeSearchCriteria);
		return serviceTypeConfigurationRepository.search(serviceTypeSearchCriteria);
	}

	private void validate(ServiceTypeConfiguration serviceTypeConfiguration) {
		validators.stream().filter(validator -> validator.canValidate(serviceTypeConfiguration))
				.forEach(v -> v.validate(serviceTypeConfiguration));
	}

	private void codeValidate(ServiceTypeConfiguration serviceTypeConfiguration) {
		validators.stream().filter(validator -> validator.canValidate(serviceTypeConfiguration))
				.forEach(v -> v.checkCode(serviceTypeConfiguration));
	}

	private void updateValidate(ServiceTypeConfiguration serviceTypeConfiguration) {
		validators.stream().filter(validator -> validator.canValidate(serviceTypeConfiguration))
				.forEach(v -> v.updateCode(serviceTypeConfiguration));
	}

	private void validateSearch(ServiceTypeConfigurationSearchCriteria serviceTypeSearchCriteria) {
		validators.stream().filter(validator -> validator.canValidater(serviceTypeSearchCriteria))
				.forEach(v -> v.validater(serviceTypeSearchCriteria));
	}

}