package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDCBResponse {

    private ResponseInfo responseInfo;

    private PropertyDCB propertyDCB;
}
