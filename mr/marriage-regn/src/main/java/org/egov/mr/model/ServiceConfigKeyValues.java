package org.egov.mr.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class ServiceConfigKeyValues {
	private List<ServiceConfigKeyValuesConfigValues> configValues = new ArrayList<ServiceConfigKeyValuesConfigValues>();
}
