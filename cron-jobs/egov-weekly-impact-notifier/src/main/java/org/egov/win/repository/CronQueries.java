package org.egov.win.repository;

import org.springframework.stereotype.Component;

public class CronQueries {
	
	public static final String GET_STATEWIDE_DATA = "";
	
	public static final String GET_PGR_DATA = "(SELECT count(DISTINCT tenantid) as ulbCovered, count(*) as totalComplaints, (SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract (epoch from NOW())) * 1000)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE status = 'closed' OR status = 'resolved' AND createdtime < ((extract (epoch from NOW())) * 1000) ) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract (epoch from NOW())) * 1000))), '%') ELSE '0%' END) as redressal), 'Today' as day FROM eg_pgr_service WHERE createdtime < ((extract (epoch from NOW())) * 1000))\n" + 
			"UNION\n" + 
			"(SELECT count(DISTINCT tenantid) as ulbCovered, count(*) as totalComplaints, (SELECT (CASE WHEN (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)) != 0 \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE status = 'closed' OR status = 'resolved' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch))), '%') ELSE '0%' END) as redressal), 'Week3' as day FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch))\n" + 
			"UNION\n" + 
			"(SELECT count(DISTINCT tenantid) as ulbCovered, count(*) as totalComplaints, (SELECT (CASE WHEN (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)) != 0\n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE status = 'closed' OR status = 'resolved' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2))), '%') ELSE '0%' END) as redressal), 'Week2' as day FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2))\n" + 
			"UNION\n" + 
			"(SELECT count(DISTINCT tenantid) as ulbCovered, count(*) as totalComplaints, (SELECT (CASE WHEN (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3)) != 0 \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE status = 'closed' OR status = 'resolved' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3))), '%') ELSE '0%' END) as redressal), 'Week1' as day FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3));";
	
	
	public static final String GET_PGR_CHANNELWISE_DATA = "(SELECT (SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract (epoch from NOW())) * 1000)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'ivr' AND createdtime < ((extract (epoch from NOW())) * 1000) ) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract (epoch from NOW())) * 1000))), '%') ELSE '0%' END) as ivr), \n" + 
			"(SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract (epoch from NOW())) * 1000)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'web' AND createdtime < ((extract (epoch from NOW())) * 1000) ) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract (epoch from NOW())) * 1000))), '%') ELSE '0%' END) as webapp),\n" + 
			"(SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract (epoch from NOW())) * 1000)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'mobileapp' AND createdtime < ((extract (epoch from NOW())) * 1000) ) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract (epoch from NOW())) * 1000))), '%') ELSE '0%' END) as mobileapp), 'Today' as day \n" + 
			"FROM eg_pgr_service WHERE createdtime < (((extract (epoch from NOW())) * 1000) * 1000)) LIMIT 1;\n" + 
			"UNION\n" + 
			"(SELECT (SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'ivr' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch))), '%') ELSE '0%' END) as redressal), \n" + 
			"(SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'web' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch))), '%') ELSE '0%' END) as redressal),\n" + 
			"(SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'mobileapp' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch))), '%') ELSE '0%' END) as redressal), 'Week3' as day \n" + 
			"FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)) LIMIT 1;\n" + 
			"UNION\n" + 
			"(SELECT (SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'ivr' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2))), '%') ELSE '0%' END) as redressal), \n" + 
			"(SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'web' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2))), '%') ELSE '0%' END) as redressal),\n" + 
			"(SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'mobileapp' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2))), '%') ELSE '0%' END) as redressal), 'Week3' as day \n" + 
			"FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)) LIMIT 1;\n" + 
			"UNION\n" + 
			"(SELECT (SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'ivr' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3))), '%') ELSE '0%' END) as redressal), \n" + 
			"(SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'web' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3))), '%') ELSE '0%' END) as redressal),\n" + 
			"(SELECT (CASE WHEN ((SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3)) != 0) \n" + 
			"THEN CONCAT(((SELECT count(*) FROM eg_pgr_service WHERE source = 'mobileapp' AND createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3)) * 100 / (SELECT count(*) FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3))), '%') ELSE '0%' END) as redressal), 'Week3' as day \n" + 
			"FROM eg_pgr_service WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3)) LIMIT 1;";
	
	
	public static final String GET_PT__DATA = "SELECT count(DISTINCT pt.tenantid) as ulbcovered, TRUNC(SUM(ins.amount) / 10000000, 3) as revenuecollected, CONCAT((count(DISTINCT pt.propertyid) / 1000), '.', MOD(count(DISTINCT pt.propertyid), 1000)) as noofpropertiescreated, 'Today' as day FROM eg_pt_property_v2 pt LEFT JOIN egcl_receiptheader rh ON pt.propertyid = SPLIT_PART(rh.consumercode, ':', 1) LEFT JOIN egcl_instrumentheader ins ON rh.transactionid = ins.transactionnumber WHERE pt.createdtime < (extract(epoch from NOW()) * 1000)\n" + 
			"UNION\n" + 
			"SELECT count(DISTINCT pt.tenantid) as ulbcovered, TRUNC(SUM(ins.amount) / 10000000, 3) as revenuecollected, CONCAT((count(DISTINCT pt.propertyid) / 1000), '.', MOD(count(DISTINCT pt.propertyid), 1000)) as noofpropertiescreated, 'Week3' as day FROM eg_pt_property_v2 pt LEFT JOIN egcl_receiptheader rh ON pt.propertyid = SPLIT_PART(rh.consumercode, ':', 1) LEFT JOIN egcl_instrumentheader ins ON rh.transactionid = ins.transactionnumber WHERE pt.createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)\n" + 
			"UNION\n" + 
			"SELECT count(DISTINCT pt.tenantid) as ulbcovered, TRUNC(SUM(ins.amount) / 10000000, 3) as revenuecollected, CONCAT((count(DISTINCT pt.propertyid) / 1000), '.', MOD(count(DISTINCT pt.propertyid), 1000)) as noofpropertiescreated, 'Week2' as day FROM eg_pt_property_v2 pt LEFT JOIN egcl_receiptheader rh ON pt.propertyid = SPLIT_PART(rh.consumercode, ':', 1) LEFT JOIN egcl_instrumentheader ins ON rh.transactionid = ins.transactionnumber WHERE pt.createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)\n" + 
			"UNION\n" + 
			"SELECT count(DISTINCT pt.tenantid) as ulbcovered, TRUNC(SUM(ins.amount) / 10000000, 3) as revenuecollected, CONCAT((count(DISTINCT pt.propertyid) / 1000), '.', MOD(count(DISTINCT pt.propertyid), 1000)) as noofpropertiescreated, 'Week1' as day FROM eg_pt_property_v2 pt LEFT JOIN egcl_receiptheader rh ON pt.propertyid = SPLIT_PART(rh.consumercode, ':', 1) LEFT JOIN egcl_instrumentheader ins ON rh.transactionid = ins.transactionnumber WHERE pt.createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3);";
	
	
	public static final String GET_TL__DATA = "SELECT count(DISTINCT tenantid) as ulbCovered, count(*) as licenseIssued, 'Today' as day FROM eg_tl_tradelicense WHERE createdtime < ((extract (epoch from NOW())) * 1000)\n" + 
			"UNION\n" + 
			"SELECT count(DISTINCT tenantid) as ulbCovered, count(*) as licenseIssued, 'Week3' as day FROM eg_tl_tradelicense WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch)\n" + 
			"UNION\n" + 
			"SELECT count(DISTINCT tenantid) as ulbCovered, count(*) as licenseIssued, 'Week2' as day FROM eg_tl_tradelicense WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 2)\n" + 
			"UNION\n" + 
			"SELECT count(DISTINCT tenantid) as ulbCovered, count(*) as licenseIssued, 'Week1' as day FROM eg_tl_tradelicense WHERE createdtime < ((extract(epoch from NOW()) * 1000) - :intervalinepoch * 3);";

}
