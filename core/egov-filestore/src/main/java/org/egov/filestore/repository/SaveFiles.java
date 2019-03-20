package org.egov.filestore.repository;

import java.util.List;

import org.egov.filestore.domain.model.Artifact;

public interface SaveFiles {
	
	/**
	 * Interface to save the files to a cloud storage. Current implmentations:
	 * 1. AWS S3
	 * 2. Azure Blob Storage
	 * 
	 * @param artifacts
	 */
	public void save(List<Artifact> artifacts);

}
