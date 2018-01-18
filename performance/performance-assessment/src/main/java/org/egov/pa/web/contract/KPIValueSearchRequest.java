package org.egov.pa.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * KPIValueSearchRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T08:37:15.865Z")

public class KPIValueSearchRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("kpiCodes")
  private List<String> kpiCodes = null;

  @JsonProperty("departmentId") 
  private Long departmentId = null; 
  
  @JsonProperty("tenantId")
  private List<String> tenantId = null;
  
  @JsonProperty("ulbs")
  private List<String> ulbList=null;

  @JsonProperty("finYear")
  private List<String> finYear = null;
  
  @JsonProperty("graphType")
  private String graphType = null;
  
  @JsonProperty("categoryId")
  private Long categoryId = null ; 
  
  @JsonProperty("needDocs")
  private Boolean needDocs = null;
  
  
  
  
  
  

  public Boolean getNeedDocs() {
	return needDocs;
}

public void setNeedDocs(Boolean needDocs) {
	this.needDocs = needDocs;
}

public Long getCategoryId() {
	return categoryId;
}

public void setCategoryId(Long categoryId) {
	this.categoryId = categoryId;
}

public List<String> getUlbList() {
	return ulbList;
}

public void setUlbList(List<String> ulbList) {
	this.ulbList = ulbList;
}

public String getGraphType() {
	return graphType;
}

public void setGraphType(String graphType) {
	this.graphType = graphType;
}

public Long getDepartmentId() {
	return departmentId;
}

public void setDepartmentId(Long departmentId) {
	this.departmentId = departmentId;
}

public KPIValueSearchRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
   * @return requestInfo
  **/

  @Valid

  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public KPIValueSearchRequest kpiCodes(List<String> kpiCodes) {
    this.kpiCodes = kpiCodes;
    return this;
  }

  public KPIValueSearchRequest addKpiCodesItem(String kpiCodesItem) {
    if (this.kpiCodes == null) {
      this.kpiCodes = new ArrayList<String>();
    }
    this.kpiCodes.add(kpiCodesItem);
    return this;
  }

   /**
   * Get kpiCodes
   * @return kpiCodes
  **/


  public List<String> getKpiCodes() {
    return kpiCodes;
  }

  public void setKpiCodes(List<String> kpiCodes) {
    this.kpiCodes = kpiCodes;
  }

  public KPIValueSearchRequest tenantId(List<String> tenantId) {
    this.tenantId = tenantId;
    return this;
  }

  public KPIValueSearchRequest addTenantIdItem(String tenantIdItem) {
    if (this.tenantId == null) {
      this.tenantId = new ArrayList<String>();
    }
    this.tenantId.add(tenantIdItem);
    return this;
  }

   /**
   * Get tenantId
   * @return tenantId
  **/


  public List<String> getTenantId() {
    return tenantId;
  }

  public void setTenantId(List<String> tenantId) {
    this.tenantId = tenantId;
  }

  public KPIValueSearchRequest finYear(List<String> finYear) {
    this.finYear = finYear;
    return this;
  }

  public KPIValueSearchRequest addFinYearItem(String finYearItem) {
    if (this.finYear == null) {
      this.finYear = new ArrayList<String>();
    }
    this.finYear.add(finYearItem);
    return this;
  }

   /**
   * Get finYear
   * @return finYear
  **/


  public List<String> getFinYear() {
    return finYear;
  }

  public void setFinYear(List<String> finYear) {
    this.finYear = finYear;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KPIValueSearchRequest kpIValueSearchRequest = (KPIValueSearchRequest) o;
    return Objects.equals(this.requestInfo, kpIValueSearchRequest.requestInfo) &&
        Objects.equals(this.kpiCodes, kpIValueSearchRequest.kpiCodes) &&
        Objects.equals(this.tenantId, kpIValueSearchRequest.tenantId) &&
        Objects.equals(this.finYear, kpIValueSearchRequest.finYear);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, kpiCodes, tenantId, finYear);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPIValueSearchRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    kpiCodes: ").append(toIndentedString(kpiCodes)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    finYear: ").append(toIndentedString(finYear)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
