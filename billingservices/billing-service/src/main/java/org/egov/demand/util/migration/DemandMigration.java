package org.egov.demand.util.migration;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.repository.DemandRepository;
import org.egov.demand.repository.rowmapper.DemandRowMapper;
import org.egov.demand.web.contract.DemandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DemandMigration {
	
	public static final String SELECT_QUERY = "SELECT * FROM (SELECT *, DENSE_RANK() OVER (ORDER BY did) offset_ FROM " 
			+ " (select d.id as did,dl.id as dlid,dl.demandid as dldemandid,"
			+ "d.consumercode as dconsumercode,d.consumertype as dconsumertype,d.taxperiodfrom as dtaxperiodfrom,"
			+ "d.taxperiodto as dtaxperiodto,U.uuid as payer,null as dbillexpirytime,"
			+ " d.businessservice as dbusinessservice,d.status as status,d.minimumamountpayable as dminimumamountpayable, "
			+ " (CASE WHEN taxheadcode='PT_DECIMAL_CEILING_CREDIT' OR taxheadcode='PT_DECIMAL_CEILING_DEBIT' THEN 'PT_ROUNDOFF' else taxheadcode END)"
			+ " as dltaxheadcode, (CASE WHEN taxheadcode IN ('PT_TIME_REBATE', 'PT_ADVANCE_CARRYFORWARD', 'PT_OWNER_EXEMPTION','PT_UNIT_USAGE_EXEMPTION'"
			+ ", 'PT_ADHOC_REBATE', 'PT_DECIMAL_CEILING_DEBIT','TL_ADHOC_REBATE') then taxamount*-1 else taxamount END) as dltaxamount,"
			+ " dl.collectionamount as dlcollectionamount, dl.createdby as dlcreatedby,dl.createdtime as dlcreatedtime,dl.lastmodifiedby as dllastmodifiedby,"
			+ " dl.lastmodifiedtime as dllastmodifiedtime,dl.tenantid as dltenantid, d.createdby as dcreatedby,d.createdtime as dcreatedtime,"
			+ " d.lastmodifiedby as dlastmodifiedby,d.lastmodifiedtime as dlastmodifiedtime,d.tenantid as dtenantid "
			+ " from egbs_demand d inner join egbs_demanddetail dl ON d.id=dl.demandid AND d.tenantid=dl.tenantid "
			+ " INNER JOIN eg_user U ON U.id::CHARACTER VARYING=d.owner"
			+ " WHERE d.businessservice IN ('TL','PT') AND d.tenantid ilike 'pb%' ) " 
			+ " result) result_offset WHERE offset_ > ? AND offset_ <= ?;";
	
	public static final String COUNT_QUERY = "select count(*) from egbs_demand where status!='CANCELLED' AND businessservice IN ('TL','PT') AND tenantid ilike 'pb%';";
	
	private Comparator<DemandDetail> demandDetailOrdercomparator;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DemandRowMapper demandRowMapper;
	
	@Autowired
	private DemandRepository demandRepository;
	
	@Value("${migration.batch.value}")
	private Integer batchSize;
	
	@PostConstruct
	public void createDemadDetailComparator() {
		
		Map<String, Integer> taxHeadOrderMap = new HashMap<>();
		taxHeadOrderMap.put("TL_TAX", 5);
		taxHeadOrderMap.put("TL_ROUNDOFF", 1);
		taxHeadOrderMap.put("TL_ADHOCE_REBATE", 0);
		taxHeadOrderMap.put("TL_ADHOC_PENALTY", 3);
		
		taxHeadOrderMap.put("PT_TIME_REBATE", 0);
		taxHeadOrderMap.put("PT_ADHOC_REBATE", 0);
		taxHeadOrderMap.put("PT_OWNER_EXEMPTION", 0);
		taxHeadOrderMap.put("PT_ADVANCE_CARRYFORWARD", 0);
		taxHeadOrderMap.put("PT_UNIT_USAGE_EXEMPTION", 0);
		
		taxHeadOrderMap.put("PT_ROUNDOFF", 1);
		
		taxHeadOrderMap.put("PT_TIME_INTEREST", 2);
		taxHeadOrderMap.put("PT_TIME_PENALTY", 3);
		taxHeadOrderMap.put("PT_ADHOC_PENALTY", 3);
		
		taxHeadOrderMap.put("PT_FIRE_CESS", 4);
		taxHeadOrderMap.put("PT_CANCER_CESS", 4);
		
		taxHeadOrderMap.put("PT_TAX", 5);
		
		
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
	
	public Map<String, String> migrateToV1() {
		
		Map<String, String> responseMap = new HashMap<>();
		
		int count = jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class);

		for(int i=0; i<count;i = i+batchSize) {

			List<Demand> demands = jdbcTemplate.query(SELECT_QUERY, new Object[] { i, i + batchSize }, demandRowMapper);
			apportionDemands(demands);
			try {
			postDemands(demands);
			}catch (Exception e) {
				addResponseToMap(demands,responseMap,e.getMessage());
			}
			addResponseToMap(demands,responseMap,"SUCCESS");
		}
		
		return null;
	}
	
	private void addResponseToMap(List<Demand> demands, Map<String, String> responseMap, String message) {

		demands.forEach(demand -> responseMap.put(demand.getId(), message));
	}

	private void postDemands(List<Demand> demands) {
		
		demandRepository.save(DemandRequest.builder().demands(demands).requestInfo(null).build());
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
			return totalCollectionAmount.subtract(tax);

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
