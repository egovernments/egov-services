package org.egov.infra.indexer.custom.pgr.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PGRIndexObject {
	
	List<ServiceIndexObject> serviceRequests = new ArrayList<>();

}
