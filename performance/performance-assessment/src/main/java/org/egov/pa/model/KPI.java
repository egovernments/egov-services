package org.egov.pa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the object for KPI Details not specific to any ULB
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T05:16:16.756Z")

public class KPI   {
  @JsonProperty("departmentId")
  private Long departmentId = null;
  
  @JsonProperty("department")
  private Department department = null; 

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;
  
  @JsonProperty("code")
  private String code = null; 
  
  @JsonProperty("category")
  private String category = null; 
  
  @JsonProperty("categoryId")
  private String categoryId = null;

  @JsonProperty("remoteSystemId")
  private String remoteSystemId = null;
  
  @JsonProperty("periodicity")
  private String periodicity = null; 
  
  @JsonProperty("targetType")
  private String targetType;

  @JsonProperty("instructions")
  private String instructions = null;
  
  @JsonProperty("financialYear")
  private String financialYear = null; 

  @JsonProperty("documentsReq")
  private List<Document> documents = null;
  
  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null; 
  

  @JsonProperty("kpiTargets") 
  private List<KpiTarget> kpiTargets = null; 
  
  
  
  


public String getCategoryId() {
	return categoryId;
}

public void setCategoryId(String categoryId) {
	this.categoryId = categoryId;
}

public String getCategory() {
	return category;
}

public void setCategory(String category) {
	this.category = category;
}

public List<KpiTarget> getKpiTargets() {
	return kpiTargets;
}

public void setKpiTargets(List<KpiTarget> kpiTargets) {
	this.kpiTargets = kpiTargets;
}

public String getPeriodicity() {
	return periodicity;
}

public void setPeriodicity(String periodicity) {
	this.periodicity = periodicity;
}

public String getTargetType() {
	return targetType;
}

public void setTargetType(String targetType) {
	this.targetType = targetType;
}

public Department getDepartment() {
	return department;
}

public void setDepartment(Department department) {
	this.department = department;
}

public AuditDetails getAuditDetails() {
	return auditDetails;
}

public void setAuditDetails(AuditDetails auditDetails) {
	this.auditDetails = auditDetails;
}

public String getFinancialYear() {
	return financialYear;
}

public void setFinancialYear(String financialYear) {
	this.financialYear = financialYear;
}


/**
* Get department
* @return department
**/
@Valid
@Pattern(regexp="^[a-zA-Z0-9:-]+$")
public String getCode() {
 return code;
}

public void setCode(String code) {
 this.code = code;
}


   /**
   * Get department
   * @return department
  **/
  @Valid

  public Long getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Long departmentId) {
    this.departmentId = departmentId;
  }

  public KPI id(String id) {
    this.id = id;
    return this;
  }

   /**
   * unique id of the KPI - will be generated from IDGen service
   * @return id
  **/
 @Pattern(regexp="^[0-9]+$")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public KPI name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the KPI
   * @return name
  **/
  
  @Size(min=0,max=64)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public KPI remoteSystemId(String remoteSystemId) {
    this.remoteSystemId = remoteSystemId;
    return this;
  }

   /**
   * Unique Code in remote system if the KPI is from remote
   * @return remoteSystemId
  **/
 @Pattern(regexp="^[a-zA-Z0-9]+$")
  public String getRemoteSystemId() {
    return remoteSystemId;
  }

  public void setRemoteSystemId(String remoteSystemId) {
    this.remoteSystemId = remoteSystemId;
  }

  public KPI instructions(String instructions) {
    this.instructions = instructions;
    return this;
  }

   /**
   * Instructions on how to achieve the target
   * @return instructions
  **/

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

 

  public KPI documents(List<Document> documents) {
    this.documents = documents;
    return this;
  }

  public KPI addDocumentsItem(Document documentsItem) {
    if (this.documents == null) {
      this.documents = new ArrayList<Document>();
    }
    this.documents.add(documentsItem);
    return this;
  }

   /**
   * Get documents
   * @return documents
  **/

  @Valid

  public List<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KPI KPI = (KPI) o;
    return Objects.equals(this.departmentId, KPI.departmentId) &&
        Objects.equals(this.id, KPI.id) &&
        Objects.equals(this.name, KPI.name) &&
        Objects.equals(this.remoteSystemId, KPI.remoteSystemId) &&
        Objects.equals(this.instructions, KPI.instructions) &&
        Objects.equals(this.documents, KPI.documents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(departmentId, id, name, remoteSystemId, instructions, financialYear, documents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KPI {\n");
    
    sb.append("    department: ").append(toIndentedString(departmentId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    remoteSystemId: ").append(toIndentedString(remoteSystemId)).append("\n");
    sb.append("    instructions: ").append(toIndentedString(instructions)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
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

