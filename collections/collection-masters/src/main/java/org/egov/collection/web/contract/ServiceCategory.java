package org.egov.collection.web.contract;



import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)
@Getter
@AllArgsConstructor
public class ServiceCategory {

    private Long id;
	private String name;
	private String code;
	private Boolean isactive;
	private String tenantId;


	public ServiceCategory(org.egov.collection.domain.model.ServiceCategory serviceCategory)
	{   id=serviceCategory.getId();
		name=serviceCategory.getName();
		code=serviceCategory.getCode();
		isactive=serviceCategory.getIsactive();
		tenantId=serviceCategory.getTenantId();
	}
	
	
	public ServiceCategory(org.egov.collection.domain.model.ServiceCategory serviceCategory,List<String>listFields)
	{
		
	if(listFields.contains("id"))
	id=serviceCategory.getId();
	else
	id=null;
	if(listFields.contains("name"))
	 name=serviceCategory.getName();
    else 
	 name=null;
    if(listFields.contains("code"))
	   code=serviceCategory.getCode();
    else
	 code=null;
    if(listFields.contains("isactive"))
    isactive=serviceCategory.getIsactive();
    else
	 isactive=null;
    if(listFields.contains("tenantId"))	
    	tenantId=serviceCategory.getTenantId();
    else
	 tenantId=null;
	}

}

