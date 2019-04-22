package org.egov.demand.util.migration;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.repository.DemandRepository;
import org.egov.demand.repository.rowmapper.DemandRowMapper;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class DemandMigration {
	
	public static final String SELECT_QUERY = "SELECT * FROM (SELECT *, DENSE_RANK() OVER (ORDER BY did) offset_ FROM " 
			+ " (select d.id as did,dl.id as dlid,dl.demandid,d.consumercode,d.consumertype,d.taxperiodfrom,d.taxperiodto,d.owner as payer,"
			+ " (CASE WHEN taxheadcode='PT_DECIMAL_CEILING_CREDIT' OR taxheadcode='PT_DECIMAL_CEILING_DEBIT' THEN 'PT_ROUNDOFF' else taxheadcode END)"
			+ " as dltaxheadcode, (CASE WHEN taxheadcode IN ('PT_TIME_REBATE', 'PT_ADVANCE_CARRYFORWARD', 'PT_OWNER_EXEMPTION','PT_UNIT_USAGE_EXEMPTION'"
			+ ", 'PT_ADHOC_REBATE', 'PT_DEIMCAL_CEILING_DEBIT','TL_ADHOC_REBATE') then taxamount*-1 else taxamount END) as dltaxamount,"
			+ " dl.collectionamount, dl.createdby as dlcreatedby,dl.createdtime as dlcreatedtime,dl.lastmodifiedby as dllastmodifiedby,"
			+ " dl.lastmodifiedtime as dllastmodifiedtime,dl.tenantid as dltenatid, d.createdby as dcreatedby,d.createdtime as dcreatedtime,"
			+ " d.lastmodifiedby as dlastmodifiedby,d.lastmodifiedtime as dlastmodifiedtime,d.tenantid as dtenantid "
			+ " from egbs_demand d inner join egbs_demanddetail dl ON d.id=dl.demandid AND d.tenantid=dl.tenantid) " 
			+ " result) result_offset WHERE offset_ > ? AND offset_ <= ?;";
	
	public static final String COUNT_QUERY = "select count(*) from egbs_demand where status!='CANCELLED';";
	
	private Comparator<DemandDetail> demandDetailOrdercomparator;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DemandRowMapper demandRowMapper;
	
	@Autowired
	private DemandRepository demandRepository;
	
	@PostConstruct
	public void createDemadDetailComparator() {
		
		Map<String, Integer> taxHeadOrderMap = new HashMap<>();
		taxHeadOrderMap.put("TL_TAX", 3);
		taxHeadOrderMap.put("TL_ROUNDOFF", 1);
		taxHeadOrderMap.put("TL_ADHOCE_REBATE", 0);
		taxHeadOrderMap.put("TL_ADHOC_PENALTY", 2);
		
		taxHeadOrderMap.put("PT_TIME_REBATE", 0);
		taxHeadOrderMap.put("PT_ADHOC_REBATE", 0);
		taxHeadOrderMap.put("PT_OWNER_EXEMPTION", 0);
		taxHeadOrderMap.put("PT_ADVANCE_CARRYFORWARD", 0);
		taxHeadOrderMap.put("PT_UNIT_USAGE_EXEMPTION", 0);
		
		taxHeadOrderMap.put("PT_ROUNDOFF", 1);
		
		taxHeadOrderMap.put("PT_TIME_INTEREST", 2);
		taxHeadOrderMap.put("PT_TIME_PENALTY", 2);
		taxHeadOrderMap.put("PT_ADHOC_PENALTY", 2);
		
		taxHeadOrderMap.put("PT_FIRE_CESS", 3);
		taxHeadOrderMap.put("PT_CANCER_CESS", 3);
		
		taxHeadOrderMap.put("PT_TAX", 4);
		
		
		demandDetailOrdercomparator = new Comparator<DemandDetail>() {

			private Map<String, Integer> orderMap = taxHeadOrderMap;

			@Override
			public int compare(DemandDetail dD1, DemandDetail dD2) {

				Integer int1 = orderMap.get(dD1.getTaxHeadMasterCode());
				Integer int2 = orderMap.get(dD2.getTaxHeadMasterCode());
				return int1.compareTo(int2);
			}
		};
		
	}
	
	public Map<String, String> migrateToV1(RequestInfoWrapper wrapper) {
		
		int count = jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class);
		int batchSize = 50;
		
		for(int i=0; i<count;i = i+batchSize) {

			List<Demand> demands = jdbcTemplate.query("", new Object[] { i, i + batchSize }, demandRowMapper);
			apportionDemands(demands);
			postDemands(demands, wrapper.getRequestInfo());
		}
		
		return null;
	}
	
	private void postDemands(List<Demand> demands, RequestInfo requestInfo) {
		
		demandRepository.save(DemandRequest.builder().demands(demands).requestInfo(requestInfo).build());
	}

	/**
	 * Apportion list of demands
	 * 
	 * @param demands
	 */
	private void apportionDemands(List<Demand> demands) {
	
		for(Demand demand : demands) {
			
			List<DemandDetail> details = demand.getDemandDetails();
			Collections.sort(details, demandDetailOrdercomparator);
			
			BigDecimal collectionAmount = BigDecimal.ZERO;
			for (DemandDetail detail : details) {

				collectionAmount = apportionDemandDetail(collectionAmount, detail);
			}
		}
	}

	/**
	 * Method to apportion the DemandDetail object 
	 * 
	 * @param totalCollectionAmount
	 * @param detail
	 */
	private BigDecimal apportionDemandDetail(BigDecimal totalCollectionAmount, DemandDetail detail) {
		BigDecimal tax = detail.getTaxAmount();

		if (BigDecimal.ZERO.compareTo(tax) > 0) {

			detail.setCollectionAmount(tax);
			return totalCollectionAmount.add(tax);

		} else if (BigDecimal.ZERO.compareTo(tax) < 0) {

			totalCollectionAmount = detail.getCollectionAmount().add(totalCollectionAmount);
			if (tax.compareTo(totalCollectionAmount) > 0) {

				detail.setCollectionAmount(totalCollectionAmount);
				return BigDecimal.ZERO;
			} else {

				detail.setCollectionAmount(tax);
				return totalCollectionAmount.subtract(tax);
			}
		}
		
		/*
		 * When taxAmount in demandDetail is zero, then the scenario is not handled
		 * considering there won't be anymore collection amount left 
		 * (or) apportion not required since the detail has only ZERO
		 */
		return totalCollectionAmount;
	}
	
}
