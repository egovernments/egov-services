package org.egov.egf.master.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.master.TestConfiguration;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.repository.FundRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class FundServiceTest { 
	
	@InjectMocks
	private FundService fundService;
	
	@Mock
	private SmartValidator validator;

	@Mock
	private FundRepository fundRepository;
	
	private BindingResult errors=new BeanPropertyBindingResult(null, null);
	
	private List<Fund> funds=new ArrayList<>();

	@Before
   public void setup()
   {
		
		 
	   
   }
	 

	@Test
	public final void test_Valid_Add() {
		
		Fund fund1=	Fund.builder()
				.id("1")
				.name("MunicipalFund")
				.code("001")
				.identifier('M')
				.active(true)
				.parent(null)
				.level(1l).build();
				 funds.add(fund1);
		fundService.add(funds, errors);  
		//verify(fundService).fetchRelated(funds);
		//verify(fundService).val(funds);
	}

	
	@Test
	public final void test_Valid_Update() {
		
		Fund fund1=	Fund.builder()
				.id("1")
				.name("MunicipalFund")
				.code("001")
				.identifier('M')
				.active(true)
				.parent(null)
				.level(1l).build();
				 funds.add(fund1);
		fundService.update(funds, errors);  
	}

	
	@Test
	public final void test_invalid_Add() {
		
		Fund fund1=	Fund.builder()
				.id("1")
				.code("001")
				.identifier('M')
				.active(true)
				.parent(null)
				.level(1l).build();
				 funds.add(fund1);
		
	 
		fundService.add(funds, errors);  
	}

 

}
