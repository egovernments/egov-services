package org.egov.egf.master.persistence.repository;

import java.util.UUID;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.master.persistence.entity.FunctionEntity;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class FunctionRepository extends JdbcRepository {
	
	
	static
	{
		System.out.println("init function");
		init(FunctionEntity.class);
		System.out.println("end  init function");
	}
	
	 
	public FunctionEntity create(FunctionEntity function) {
		 
		function.setId(UUID.randomUUID().toString().replace("-", ""));
		super.create(function);
		return function;
	}
 

}