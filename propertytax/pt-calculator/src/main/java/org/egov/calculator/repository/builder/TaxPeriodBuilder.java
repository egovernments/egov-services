package org.egov.calculator.repository.builder;

/**
 * <h1>TaxPeriodBuilder</h1>This class contains all the queries related to the
 * tax period
 * 
 * @author Prasad
 *
 */
public class TaxPeriodBuilder {

	public static final String INSERT_TAX_PERIOD_QUERY = "INSERT INTO egpt_mstr_taxperiods "
			+ "(tenantid,fromdate,todate,code,periodType,financialYear,createdby,lastmodifiedby,"
			+ "createdtime,lastmodifiedtime) VALUES(?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_TAX_PERIOD_QUERY = "UPDATE egpt_mstr_taxperiods SET tenantid=?,fromdate=?"
			+ ",todate=?,code=?,periodType=?,financialYear=?,createdby=?,lastmodifiedby=?,createdtime=?,"
			+ "lastmodifiedtime=? WHERE tenantid =?";

	public static String getSearchQuery(String tenantId, String validDate, String code) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("SELECT * FROM egpt_mstr_taxperiods WHERE tenantId = '" + tenantId + "'");
		
		if ( code!=null && !code.isEmpty())
			searchSql.append(" AND code ='" + code + "'");
			
		searchSql.append( " AND ('" + validDate + "' BETWEEN fromdate AND  todate )");

		return searchSql.toString();
	}
	
	public static String getTaxperiodsByDateAndTenantId(String tenantId, String fromDate,String toDate) {
	        // TODO Auto-generated method stub
	        StringBuffer searchSql = new StringBuffer();
	        searchSql.append("SELECT * FROM egpt_mstr_taxperiods WHERE tenantId = '" + tenantId + "'");
	        searchSql.append(" AND (fromdate::date>='" + fromDate + "' And  todate::date<='"+toDate+"')");
	        return searchSql.toString();
	    }

	public static String getToDateForTaxCalculation(String tenantId,String date){
	    String searchQuery="SELECT * FROM  egpt_mstr_taxperiods WHERE  tenantId='"+tenantId+"' "
	            + "and todate::date  >= to_date('"+date+"','dd/MM/yyyy') ORDER BY todate ASC LIMIT 1";
	    return searchQuery;
	    
	}
	
	public static String getFromDateForTaxCalculation(String tenantId,String date){
            String searchQuery="SELECT * FROM  egpt_mstr_taxperiods WHERE  tenantId='"+tenantId+"' "
                    + "and fromdate::date  <= to_date('"+date+"','dd/MM/yyyy') ORDER BY todate DESC LIMIT 1";
            return searchQuery;
            
        }
}
