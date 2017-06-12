package org.egov.domain.service;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.ServiceTypeRepository;
import org.egov.persistence.repository.TenantRepository;
import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private EmailService emailService;
    private SMSService smsService;
    private ServiceTypeRepository serviceTypeRepository;
    private TenantRepository tenantRepository;
    private EmployeeRepository employeeRepository;

    public NotificationService(EmailService emailService,
                               SMSService smsService,
                               ServiceTypeRepository serviceTypeRepository,
                               TenantRepository tenantRepository, EmployeeRepository employeeRepository) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.serviceTypeRepository = serviceTypeRepository;
        this.tenantRepository = tenantRepository;
        this.employeeRepository = employeeRepository;
    }

    public void notify(SevaRequest sevaRequest) {
        final NotificationContext context = getNotificationContext(sevaRequest);
        this.smsService.send(context);
        this.emailService.send(context);
    }

    private NotificationContext getNotificationContext(SevaRequest sevaRequest) {
        final ServiceType serviceType = this.serviceTypeRepository
            .getServiceTypeByCode(sevaRequest.getServiceTypeCode(), sevaRequest.getTenantId());
        final Tenant tenant = this.tenantRepository.fetchTenantById(sevaRequest.getTenantId());
        final Employee employee = this.employeeRepository
            .getEmployeeByPosition(sevaRequest.getAssigneeId(), sevaRequest.getTenantId());
        final Employee previousEmployee = getPreviousEmployee(sevaRequest);

        return NotificationContext.builder()
            .employee(employee)
            .previousEmployee(previousEmployee)
            .serviceType(serviceType)
            .tenant(tenant)
            .sevaRequest(sevaRequest)
            .build();
    }

    private Employee getPreviousEmployee(SevaRequest sevaRequest) {
        if (sevaRequest.getPreviousAssignee() == null) {
            return null;
        }
        return this.employeeRepository
            .getEmployeeByPosition(sevaRequest.getPreviousAssignee(), sevaRequest.getTenantId());
    }
}
