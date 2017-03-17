package org.egov.commons.service;

import java.util.List;

import org.egov.commons.model.Uom;
import org.egov.commons.repository.UomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UomService {

	@Autowired
	private UomRepository uomRepository;
	
	public List<Uom> search(){
		return uomRepository.search();
	}
}
