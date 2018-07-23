package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.pt.calculator.util.CalculatorConstants;
import org.egov.pt.calculator.util.CalculatorUtils;
import org.egov.pt.calculator.web.models.demand.BillAccountDetail;
import org.egov.pt.calculator.web.models.demand.BillRequest;
import org.egov.pt.calculator.web.models.demand.BillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;

/**
 * Deals with the functionality that are performed 
 * 
 * before the time of bill generation(before payment)  
 * 
 * or at the time of bill apportioning(after payment)
 * 
 * @author kavi elrey
 *
 */
@Service
public class PayService {

	@Autowired
	private CalculatorUtils utils;

	@Autowired
	private MasterDataService mDService;
	
	/**
	 * Updates the incoming demand with latest rebate and penalty values if applicable
	 * 
	 * If the demand details are not already present then new demand details will be added
	 * 
	 * @param demand
	 * @param assessmentYear
	 * @return
	 */
	public Map<String, BigDecimal> applyPenaltyAndRebate(BigDecimal taxAmt, String assessmentYear,
			Map<String, JSONArray> timeBasedExmeptionMasterMap) {

		Map<String, BigDecimal> estimates = new HashMap<>();
		BigDecimal rebate = getRebate(taxAmt, assessmentYear,
				timeBasedExmeptionMasterMap.get(CalculatorConstants.REBATE_MASTER));
		BigDecimal penalty = BigDecimal.ZERO;
		if (rebate.doubleValue() == 0.0)
			getPenalty(taxAmt, assessmentYear, timeBasedExmeptionMasterMap.get(CalculatorConstants.PENANLTY_MASTER));
		estimates.put(CalculatorConstants.PT_TIME_REBATE, rebate);
		estimates.put(CalculatorConstants.PT_TIME_PENALTY, penalty);
		return estimates;
	}

	/**
	 * Returns the Amount of Rebate that can be applied on the given tax amount for
	 * the given period
	 * 
	 * @param taxAmt
	 * @param assessmentYear
	 * @return
	 */
	public BigDecimal getRebate(BigDecimal taxAmt, String assessmentYear, JSONArray rebateMasterList) {

		BigDecimal rebateAmt = BigDecimal.ZERO;
		Map<String, Object> rebate = mDService.getApplicableMasterFromList(assessmentYear, rebateMasterList);

		if (null != rebate) {

			String[] time = ((String) rebate.get(CalculatorConstants.ENDING_DATE_REBATE)).split("/");
			Calendar cal = Calendar.getInstance();
			setDateToCalendar(assessmentYear, time, cal);

			if (cal.getTime().compareTo(new Date()) >= 0) {
				
				Double exemptionRate = (Double) rebate.get(CalculatorConstants.RATE_FIELD_NAME);
				Double exempMaxRate = (Double) rebate.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME);
				rebateAmt = rebateAmt.add(taxAmt.multiply(BigDecimal.valueOf(exemptionRate / 100)));
				if (null != exempMaxRate && !exemptionRate.equals(100.0)
						&& rebateAmt.compareTo(BigDecimal.valueOf(exempMaxRate)) > 0)
					rebateAmt = BigDecimal.valueOf(exempMaxRate);
			}
		}
		return rebateAmt;
	}

	/**
	 * Returns the Amount of penalty that has to be applied on the given tax amount for the given period
	 * 
	 * @param taxAmt
	 * @param assessmentYear
	 * @return
	 */
	public BigDecimal getPenalty(BigDecimal taxAmt, String assessmentYear, JSONArray penaltyMasterList) {

		BigDecimal penaltyAmt = BigDecimal.ZERO;
		Map<String, Object> penalty = mDService.getApplicableMasterFromList(assessmentYear, penaltyMasterList);
		if (null != penalty) {

			String[] time = ((String) penalty.get(CalculatorConstants.STARTING_DATE_PENALTY)).split("/");
			Calendar cal = Calendar.getInstance();
			setDateToCalendar(assessmentYear, time, cal);

			if (cal.getTime().compareTo(new Date()) < 0) {

				Double exemptionRate = (Double) penalty.get(CalculatorConstants.RATE_FIELD_NAME);
				Double exempMinRate = (Double) penalty.get(CalculatorConstants.MIN_AMOUNT_FIELD_NAME);
				penaltyAmt = penaltyAmt.add(taxAmt.multiply(BigDecimal.valueOf(exemptionRate / 100)));
				if (null != exempMinRate && !exemptionRate.equals(100.0)
						&& penaltyAmt.compareTo(BigDecimal.valueOf(exempMinRate)) < 0)
					penaltyAmt = BigDecimal.valueOf(exempMinRate);
			}
		}
		return penaltyAmt;
	}
	
	/**
	 * Return the BillResponse which contains apportioned bills
	 * @param billRequest
	 * @return
	 */
	public BillResponse apportionBills(BillRequest billRequest) {

		billRequest.getBills().forEach(bill -> bill.getBillDetails().forEach(detail -> {
			BigDecimal amtPaid = detail.getAmountPaid();
			apportionBillAccountDetails(detail.getBillAccountDetails(), amtPaid);
		}));
		// add response info FIXME 
		return BillResponse.builder().bill(billRequest.getBills()).build();
	}

	/**
	 * Apportions the amount paid to the bill account details based on the tax head codes priority
	 * 
	 * The Anonymous comparator uses the priority map to decide the precedence
	 * 
	 * Once the list is sorted based on precedence the amount will apportioned appropriately
	 * 
	 * @param billAccountDetails
	 * @param amtPaid
	 */
	private void apportionBillAccountDetails(List<BillAccountDetail> billAccountDetails, BigDecimal amtPaid) {

		Collections.sort(billAccountDetails, new Comparator<BillAccountDetail>() {
			final Map<String, Integer> taxHeadpriorityMap = utils.getTaxHeadApportionPriorityMap();
			@Override
			public int compare(BillAccountDetail arg0, BillAccountDetail arg1) {
				String taxHead0 = arg0.getAccountDescription().split("-")[0];
				String taxHead1 = arg1.getAccountDescription().split("-")[0];
				return taxHeadpriorityMap.get(taxHead0) - taxHeadpriorityMap.get(taxHead1);
			}
		});

		/*
		 * amtRemaining is the total amount left to apportioned if amtRemaining is zero
		 * then break the for loop if the amountToBePaid is greater then amount left
		 * then set amtRemaining to collectedAmount(creditAmount) if the amtRemaining is
		 * Greater than amountToBeCollected then subtract amtToBecollected from
		 * amtRemaining and set the same to collectedAmount(creditAmount)
		 */
		BigDecimal amtRemaining = amtPaid;
		for (BillAccountDetail billAccountDetail : billAccountDetails) {
			if (BigDecimal.ZERO.compareTo(amtRemaining) < 0) {
				BigDecimal amtToBePaid = billAccountDetail.getCrAmountToBePaid();
				if (amtToBePaid.compareTo(amtRemaining) >= 0) {
					billAccountDetail.setCreditAmount(amtRemaining);
					amtRemaining = BigDecimal.ZERO;
				} else if (amtToBePaid.compareTo(amtRemaining) < 0) {
					amtRemaining = amtRemaining.subtract(amtToBePaid);
				}
			} else
				break;
		}
	}

	/**
	 * Sets the date in to calendar based on the month and date value present in the time array
	 * 
	 * @param assessmentYear
	 * @param time
	 * @param cal
	 */
	private void setDateToCalendar(String assessmentYear, String[] time, Calendar cal) {
		
		Integer day = Integer.valueOf(time[0]);
		Integer month = Integer.valueOf(time[0]);
		Integer year = Integer.valueOf(assessmentYear.split("-")[0]);
		if (month <= 3) year += 1;
		cal.set(year, month, day);
	}

}
