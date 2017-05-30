package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

	private Integer id;
	
	private DocumentType documentType;
	
	private String fileStore;
}
