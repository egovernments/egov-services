package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.pt.calculator.util.CalculatorConstants;
import org.egov.pt.calculator.util.CalculatorUtils;
import org.egov.pt.calculator.web.models.TaxHeadEstimate;
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
	 * Updates the incoming demand with latest rebate, penalty and interest values if applicable
	 * 
	 * If the demand details are not already present then new demand details will be added
	 * 
	 * @param demand
	 * @param assessmentYear
	 * @return
	 */
	public Map<String, BigDecimal> applyPenaltyRebateAndInterest(BigDecimal taxAmt, String assessmentYear,
			Map<String, JSONArray> timeBasedExmeptionMasterMap) {

		Map<String, BigDecimal> estimates = new HashMap<>();
		BigDecimal rebate = getRebate(taxAmt, assessmentYear,
				timeBasedExmeptionMasterMap.get(CalculatorConstants.REBATE_MASTER));
		
		BigDecimal penalty = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;

		if (rebate.equals(BigDecimal.ZERO)) {
			getPenalty(taxAmt, assessmentYear, timeBasedExmeptionMasterMap.get(CalculatorConstants.PENANLTY_MASTER));
			interest = getInterest(taxAmt.subtract(rebate), assessmentYear,
					timeBasedExmeptionMasterMap.get(CalculatorConstants.INTEREST_MASTER));
		}

		estimates.put(CalculatorConstants.PT_TIME_REBATE, rebate);
		estimates.put(CalculatorConstants.PT_TIME_PENALTY, penalty);
		estimates.put(CalculatorConstants.PT_TIME_INTEREST, interest);
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

		if (null == rebate) return rebateAmt;

		String[] time = ((String) rebate.get(CalculatorConstants.ENDING_DATE_APPLICABLES)).split("/");
		Calendar cal = Calendar.getInstance();
		setDateToCalendar(assessmentYear, time, cal);

		if (cal.getTimeInMillis() > System.currentTimeMillis()) {

			BigDecimal rate = null != rebate.get(CalculatorConstants.RATE_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) rebate.get(CalculatorConstants.RATE_FIELD_NAME)).doubleValue()) : null;

			BigDecimal maxAmt = null != rebate.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) rebate.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME)).doubleValue())	: null;

			if (null != rate)
				rebateAmt = rebateAmt.add(taxAmt.multiply(rate.divide(CalculatorConstants.HUNDRED)));

			if (null != maxAmt && rebateAmt.compareTo(maxAmt) > 0)
				rebateAmt = maxAmt;
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
		if (null == penalty) return penaltyAmt;

		String[] time = ((String) penalty.get(CalculatorConstants.STARTING_DATE_APPLICABLES)).split("/");
		Calendar cal = Calendar.getInstance();
		setDateToCalendar(assessmentYear, time, cal);

		if (cal.getTimeInMillis() < System.currentTimeMillis()) {

			BigDecimal rate = null != penalty.get(CalculatorConstants.RATE_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) penalty.get(CalculatorConstants.RATE_FIELD_NAME)).doubleValue()) : null;

			BigDecimal minAmt = null != penalty.get(CalculatorConstants.MIN_AMOUNT_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) penalty.get(CalculatorConstants.MIN_AMOUNT_FIELD_NAME)).doubleValue()) : null;

			if (null != rate)
				penaltyAmt = penaltyAmt.add(taxAmt.multiply(rate.divide(CalculatorConstants.HUNDRED)));

			if (null != minAmt && penaltyAmt.compareTo(minAmt) < 0)
				penaltyAmt = minAmt;
		}

		return penaltyAmt;
	}

	/**
	 * Returns the Amount of penalty that has to be applied on the given tax amount for the given period
	 * 
	 * @param taxAmt
	 * @param assessmentYear
	 * @return
	 */
	public BigDecimal getInterest(BigDecimal taxAmt, String assessmentYear, JSONArray interestMasterList) {

		BigDecimal interestAmt = BigDecimal.ZERO;
		Map<String, Object> interestMap = mDService.getApplicableMasterFromList(assessmentYear, interestMasterList);
		if (null == interestMap) return interestAmt;

		String[] time = ((String) interestMap.get(CalculatorConstants.STARTING_DATE_APPLICABLES)).split("/");
		Calendar cal = Calendar.getInstance();
		setDateToCalendar(assessmentYear, time, cal);

		if (cal.getTimeInMillis() < System.currentTimeMillis()) {

			BigDecimal rate = null != interestMap.get(CalculatorConstants.RATE_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) interestMap.get(CalculatorConstants.RATE_FIELD_NAME)).doubleValue()) : null;

			BigDecimal minAmt = null != interestMap.get(CalculatorConstants.MIN_AMOUNT_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) interestMap.get(CalculatorConstants.MIN_AMOUNT_FIELD_NAME)).doubleValue()) : null;
					
			BigDecimal maxAmt = null != interestMap.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) interestMap.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME)).doubleValue()) : null;

			if (null != rate)
				interestAmt = interestAmt.add(taxAmt.multiply(rate.divide(CalculatorConstants.HUNDRED)));

			if (null != minAmt && interestAmt.compareTo(minAmt) < 0)
				interestAmt = minAmt;

			else if (null != maxAmt && interestAmt.compareTo(maxAmt) > 0)
				interestAmt = maxAmt;
		}

		return interestAmt;
	}
	
	/**
	 * Return the BillResponse which contains apportioned bills
	 * 
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
				String taxHead0 = arg0.getAccountDescription().split(CalculatorConstants.PT_CONSUMER_CODE_SEPARATOR)[0];
				String taxHead1 = arg1.getAccountDescription().split(CalculatorConstants.PT_CONSUMER_CODE_SEPARATOR)[0];

				Integer value0 = taxHeadpriorityMap.get(CalculatorConstants.MAX_PRIORITY_VALUE);
				Integer value1 = taxHeadpriorityMap.get(CalculatorConstants.MAX_PRIORITY_VALUE);

				if (null != taxHeadpriorityMap.get(taxHead0))
					value0 = taxHeadpriorityMap.get(taxHead0);

				if (null != taxHeadpriorityMap.get(taxHead1))
					value1 = taxHeadpriorityMap.get(taxHead1);

				return value0 - value1;
			}
		});

		/*
		 * amtRemaining is the total amount left to apportioned if amtRemaining is zero
		 * then break the for loop
		 * 
		 * if the amountToBePaid is greater then amtRemaining then set amtRemaining to
		 * collectedAmount(creditAmount)
		 * 
		 * if the amtRemaining is Greater than amountToBeCollected then subtract
		 * amtToBecollected from amtRemaining and set the same to
		 * collectedAmount(creditAmount)
		 */
		BigDecimal amtRemaining = amtPaid;
		for (BillAccountDetail billAccountDetail : billAccountDetails) {
			if (BigDecimal.ZERO.compareTo(amtRemaining) < 0) {
				BigDecimal amtToBePaid = billAccountDetail.getCrAmountToBePaid();
				if (amtToBePaid.compareTo(amtRemaining) >= 0) {
					billAccountDetail.setCreditAmount(amtRemaining);
					amtRemaining = BigDecimal.ZERO;
				} else if (amtToBePaid.compareTo(amtRemaining) < 0) {
					billAccountDetail.setCreditAmount(amtToBePaid);
					amtRemaining = amtRemaining.subtract(amtToBePaid);
				}
			} else break;
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
		Integer month = Integer.valueOf(time[1]);
		Integer year = Integer.valueOf(assessmentYear.split("-")[0]);
		if (month <= 3) year += 1;
		cal.set(year, month, day);
	}

	/**
	 * Decimal is ceiled for all the tax heads
	 * 
	 * if the decimal is greater than 0.5 upper bound will be applied
	 * 
	 * else if decimal is lesser than 0.5 lower bound is applied
	 * 
	 * @param estimates
	 */
	public TaxHeadEstimate roundOfDecimals(BigDecimal creditAmount, BigDecimal debitAmount) {

		BigDecimal roundOffPos = BigDecimal.ZERO;
		BigDecimal roundOffNeg = BigDecimal.ZERO;

		BigDecimal result = creditAmount.subtract(debitAmount);
		BigDecimal roundOffAmount = result.setScale(2, 2);
		BigDecimal reminder = roundOffAmount.remainder(BigDecimal.ONE);

		if (reminder.doubleValue() >= 0.5)
			roundOffPos = roundOffPos.add(BigDecimal.ONE.subtract(reminder));
		else if (reminder.doubleValue() < 0.5)
			roundOffNeg = roundOffNeg.add(reminder);

		if (roundOffPos.doubleValue() > 0)
			return TaxHeadEstimate.builder().estimateAmount(roundOffPos)
					.taxHeadCode(CalculatorConstants.PT_DECIMAL_CEILING_CREDIT).build();
		else if (roundOffNeg.doubleValue() > 0)
			return TaxHeadEstimate.builder().estimateAmount(roundOffNeg)
					.taxHeadCode(CalculatorConstants.PT_DECIMAL_CEILING_DEBIT).build();
		else
			return null;
	}
}
