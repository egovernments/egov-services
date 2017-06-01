/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.validator;

import java.util.Iterator;
import java.util.List;

import org.egov.wcms.model.Donation;
import org.egov.wcms.repository.DonationRepository;
import org.egov.wcms.service.DonationService;
import org.egov.wcms.web.contract.DonationGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewConnectionValidator {
	
	@Autowired
	private DonationService donationService; 
	
	public static final Logger LOGGER = LoggerFactory.getLogger(DonationRepository.class);
	
	
	public boolean validateNewConnectionBusinessRules(){
		return validateDonationAmount(); 
	}
	
	@SuppressWarnings("rawtypes")
	private boolean validateDonationAmount(){
		String donationCharges = "1000";   // Receive this from Connection Request
		List<Donation> donationList = donationService.getDonationList(prepareDonationGetRequest());
		Iterator itr = donationList.iterator();
		while(itr.hasNext()){
			Donation donation = (Donation) itr.next();
			if(donationCharges.equals(donation.getDonationAmount())){
				return true;
			}
		}
		return false;
	}
	
	private DonationGetRequest prepareDonationGetRequest(){
		// Receive new connection request as a parameter for this method
		// Then using the values in the New Connection Request, prepare a Donation Get Request Object
		// Pass this Object to Get Method of Donation Service
		DonationGetRequest donationGetRequest = new DonationGetRequest();
		donationGetRequest.setPropertyType("RES");
		donationGetRequest.setUsageType("COM");
		donationGetRequest.setCategoryType("BPL");
		donationGetRequest.setMaxHSCPipeSize("12/2");
		donationGetRequest.setMinHSCPipeSize("5/3");
		donationGetRequest.setTenantId("DEFAULT");
		return donationGetRequest; 
	}
}
