package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in DocumentTypeResponse
 * 
 * 
 * @author Shubham Pratap Singh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentTypeResponse {

	private ResponseInfo responseInfo;

	private List<DocumentType> documentTypes;
}