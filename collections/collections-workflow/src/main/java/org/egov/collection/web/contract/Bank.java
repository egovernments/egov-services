package org.egov.collection.web.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Setter
@Getter
@ToString
public class Bank   {
	
  private Long id;

  private String code;

  private String name;

  private String description;

  private Boolean active;

  private String type;

}

