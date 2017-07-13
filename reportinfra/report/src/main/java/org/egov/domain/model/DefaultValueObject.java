package org.egov.domain.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swagger.model.LinkedReport;
import org.egov.swagger.model.SearchColumn;
import org.egov.swagger.model.SearchParam;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class DefaultValueObject {
  private LinkedReport linkedReport;
  private List<SearchColumn> searchParams;

}
