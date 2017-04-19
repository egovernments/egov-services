package org.egov.tenant.domain.model;


import lombok.*;
import org.egov.tenant.domain.exception.InvalidTenantDetailsException;

import static org.springframework.util.ObjectUtils.isEmpty;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Tenant {

    private Long id;
    private String code;
    private String description;
    private String logoId;
    private String imageId;
    private String domainUrl;
    private TenantType type;
    @Setter
    private City city;

    public void validate() {
        if (
                isCityAbsent() ||
                !city.isValid() ||
                isCodeAbsent() ||
                isCodeOfInvalidLength() ||
                isLogoIdAbsent() ||
                isImageIdAbsent() ||
                isTypeAbsent()
            ) {
            throw new InvalidTenantDetailsException(this);
        }
    }

    public boolean isCityAbsent() {
        return isEmpty(city);
    }

    public boolean isCodeAbsent() {
        return isEmpty(code);
    }

    public boolean isCodeOfInvalidLength() {
        return code.length() > 256;
    }

    public boolean isLogoIdAbsent() {
        return isEmpty(logoId);
    }

    public boolean isImageIdAbsent() {
        return isEmpty(imageId);
    }

    public boolean isTypeAbsent() {
        return isEmpty(type);
    }
}
