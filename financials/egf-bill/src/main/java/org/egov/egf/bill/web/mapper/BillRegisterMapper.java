package org.egov.egf.bill.web.mapper;

import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.web.contract.BillRegisterContract;
import org.egov.egf.bill.web.contract.BillRegisterSearchContract;

public class BillRegisterMapper {

	public BillRegister toDomain(BillRegisterContract contract) {
		
		BillRegister billRegister = new BillRegister();
		
		billRegister.setId(contract.getId());
		billRegister.setBillType(contract.getBillType());
		billRegister.setBillSubType(contract.getBillSubType());
		billRegister.setBillNumber(contract.getBillNumber());
		billRegister.setBillDate(contract.getBillDate());
		billRegister.setBillAmount(contract.getBillAmount());
		billRegister.setPassedAmount(contract.getPassedAmount());
		billRegister.setModuleName(contract.getModuleName());
		billRegister.setStatus(contract.getStatus());
		billRegister.setFund(contract.getFund());
		billRegister.setFunction(contract.getFunction());
		billRegister.setFundsource(contract.getFundsource());
		billRegister.setScheme(contract.getScheme());
		billRegister.setSubScheme(contract.getSubScheme());
		billRegister.setFunctionary(contract.getFunctionary());
		billRegister.setDivision(contract.getDivision());
		billRegister.setDepartment(contract.getDepartment());
		billRegister.setSourcePath(contract.getSourcePath());
		billRegister.setBudgetCheckRequired(contract.getBudgetCheckRequired());
		billRegister.setBudgetAppropriationNo(contract.getBudgetAppropriationNo());
		billRegister.setPartyBillNumber(contract.getPartyBillNumber());
		billRegister.setPartyBillDate(contract.getPartyBillDate());
		billRegister.setDescription(contract.getDescription());
		
		return billRegister;
	}
	
	public BillRegisterContract toContract(BillRegister billRegister) {
		
		BillRegisterContract contract = new BillRegisterContract();
		
		contract.setId(billRegister.getId());
		contract.setBillType(billRegister.getBillType());
		contract.setBillSubType(billRegister.getBillSubType());
		contract.setBillNumber(billRegister.getBillNumber());
		contract.setBillDate(billRegister.getBillDate());
		contract.setBillAmount(billRegister.getBillAmount());
		contract.setPassedAmount(billRegister.getPassedAmount());
		contract.setModuleName(billRegister.getModuleName());
		contract.setStatus(billRegister.getStatus());
		contract.setFund(billRegister.getFund());
		contract.setFunction(billRegister.getFunction());
		contract.setFundsource(billRegister.getFundsource());
		contract.setScheme(billRegister.getScheme());
		contract.setSubScheme(billRegister.getSubScheme());
		contract.setFunctionary(billRegister.getFunctionary());
		contract.setDivision(billRegister.getDivision());
		contract.setDepartment(billRegister.getDepartment());
		contract.setSourcePath(billRegister.getSourcePath());
		contract.setBudgetCheckRequired(billRegister.getBudgetCheckRequired());
		contract.setBudgetAppropriationNo(billRegister.getBudgetAppropriationNo());
		contract.setPartyBillNumber(billRegister.getPartyBillNumber());
		contract.setPartyBillDate(billRegister.getPartyBillDate());
		contract.setDescription(billRegister.getDescription());
		
		return contract;
	}
	
	public BillRegisterSearch toSearchDomain(BillRegisterSearchContract contract) {

		BillRegisterSearch billRegisterSearch = new BillRegisterSearch();
		
		billRegisterSearch.setId(contract.getId());
		billRegisterSearch.setBillType(contract.getBillType());
		billRegisterSearch.setBillSubType(contract.getBillSubType());
		billRegisterSearch.setBillNumber(contract.getBillNumber());
		billRegisterSearch.setBillDate(contract.getBillDate());
		billRegisterSearch.setBillAmount(contract.getBillAmount());
		billRegisterSearch.setPassedAmount(contract.getPassedAmount());
		billRegisterSearch.setModuleName(contract.getModuleName());
		billRegisterSearch.setStatus(contract.getStatus());
		billRegisterSearch.setFund(contract.getFund());
		billRegisterSearch.setFunction(contract.getFunction());
		billRegisterSearch.setFundsource(contract.getFundsource());
		billRegisterSearch.setScheme(contract.getScheme());
		billRegisterSearch.setSubScheme(contract.getSubScheme());
		billRegisterSearch.setFunctionary(contract.getFunctionary());
		billRegisterSearch.setDivision(contract.getDivision());
		billRegisterSearch.setDepartment(contract.getDepartment());
		billRegisterSearch.setSourcePath(contract.getSourcePath());
		billRegisterSearch.setBudgetCheckRequired(contract.getBudgetCheckRequired());
		billRegisterSearch.setBudgetAppropriationNo(contract.getBudgetAppropriationNo());
		billRegisterSearch.setPartyBillNumber(contract.getPartyBillNumber());
		billRegisterSearch.setPartyBillDate(contract.getPartyBillDate());
		billRegisterSearch.setDescription(contract.getDescription());
		
		return billRegisterSearch;
	}
	
	public BillRegisterSearchContract toSearchContract(BillRegisterSearch billRegisterSearch) {
		
		BillRegisterSearchContract contract = new BillRegisterSearchContract();
		
		contract.setId(billRegisterSearch.getId());
		contract.setBillType(billRegisterSearch.getBillType());
		contract.setBillSubType(billRegisterSearch.getBillSubType());
		contract.setBillNumber(billRegisterSearch.getBillNumber());
		contract.setBillDate(billRegisterSearch.getBillDate());
		contract.setBillAmount(billRegisterSearch.getBillAmount());
		contract.setPassedAmount(billRegisterSearch.getPassedAmount());
		contract.setModuleName(billRegisterSearch.getModuleName());
		contract.setStatus(billRegisterSearch.getStatus());
		contract.setFund(billRegisterSearch.getFund());
		contract.setFunction(billRegisterSearch.getFunction());
		contract.setFundsource(billRegisterSearch.getFundsource());
		contract.setScheme(billRegisterSearch.getScheme());
		contract.setSubScheme(billRegisterSearch.getSubScheme());
		contract.setFunctionary(billRegisterSearch.getFunctionary());
		contract.setDivision(billRegisterSearch.getDivision());
		contract.setDepartment(billRegisterSearch.getDepartment());
		contract.setSourcePath(billRegisterSearch.getSourcePath());
		contract.setBudgetCheckRequired(billRegisterSearch.getBudgetCheckRequired());
		contract.setBudgetAppropriationNo(billRegisterSearch.getBudgetAppropriationNo());
		contract.setPartyBillNumber(billRegisterSearch.getPartyBillNumber());
		contract.setPartyBillDate(billRegisterSearch.getPartyBillDate());
		contract.setDescription(billRegisterSearch.getDescription());
		
		return contract;
	}
}
