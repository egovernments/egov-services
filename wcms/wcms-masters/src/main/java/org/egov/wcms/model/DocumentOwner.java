package org.egov.wcms.model;

import javax.validation.constraints.NotNull;


import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder

public class DocumentOwner {

	@NotNull
	private Document document;
	
	@NotNull
	private String name;
	
	@NotNull
	private String fileStoreId;
}
