package org.egov.demand.repository.builder;

import java.util.List;
import java.util.Set;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaxHeadMasterQueryBuilder {
	
	@Autowired
	private ApplicationProperties applicationProperties;
	

//	private static final String BASE_QUERY="select * from egbs_taxheadmaster";
	
	private static final String BASE_QUERY="SELECT *,taxhead.id as taxheadId, "
			+ "taxhead.tenantid as taxheadTenantid, taxhead.service taxheadService,"
			+ " glcode.id as glCodeId,glcode.tenantid as glCodeTenantId,glcode.service as glCodeService ,"
			+ "taxhead.createdby as taxcreatedby, taxhead.createdtime as taxcreatedtime,taxhead.lastmodifiedby as taxlastmodifiedby,"
			+ " taxhead.lastmodifiedtime as taxlastmodifiedtime,glcode.createdby as glcreatedby, glcode.createdtime as glcreatedtime,"
			+ " glcode.lastmodifiedby as gllastmodifiedby, glcode.lastmodifiedtime as gllastmodifiedtime"
			+ " FROM egbs_taxheadmaster taxhead INNER Join egbs_glcodemaster glcode "
			+ "on taxhead.code=glcode.taxhead and taxhead.tenantid=glcode.tenantid "
			+ "WHERE taxhead.tenantId = ? ";
	
	public String getQuery(final TaxHeadMasterCriteria searchTaxHead, final List<Object> preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		log.info("get query");
	    addWhereClause(selectQuery, preparedStatementValues, searchTaxHead);
		addPagingClause(selectQuery, preparedStatementValues, searchTaxHead);
		log.info("Query from taxHeadMaster querybuilde for search : " + selectQuery);
		return selectQuery.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final TaxHeadMasterCriteria searchTaxHead) {
		if(searchTaxHead.getTenantId()==null&&searchTaxHead.getService()==null&&searchTaxHead.getName()==null
				&&searchTaxHead.getCode()==null&&searchTaxHead.getCategory()==null)
			return;
		preparedStatementValues.add(searchTaxHead.getTenantId());
		
		if(searchTaxHead.getService()!=null){
			selectQuery.append(" and taxhead.service = ?");
			preparedStatementValues.add(searchTaxHead.getService());
		}
		
		if (searchTaxHead.getName() != null) {
			selectQuery.append(" and taxhead.name like ?");
			preparedStatementValues.add("%" + searchTaxHead.getName() + "%");
		}

		if (searchTaxHead.getId()!=null && !searchTaxHead.getId().isEmpty()) {
			selectQuery.append(" and taxhead.id IN (" + getIdQuery(searchTaxHead.getId()));
		}else if(searchTaxHead.getCode()!=null && !searchTaxHead.getCode().isEmpty()) {
			selectQuery.append(" and taxhead.code IN ("+ getIdQuery(searchTaxHead.getCode()));
		}
		if (searchTaxHead.getCategory() != null) {
			selectQuery.append(" and taxhead.category = ?");
			preparedStatementValues.add(searchTaxHead.getCategory());
		}
		if (searchTaxHead.getIsActualDemand() != null) {
			selectQuery.append(" and taxhead.isActualDemand = ?");
			preparedStatementValues.add(searchTaxHead.getIsActualDemand());
		}
		if (searchTaxHead.getIsDebit() != null) {
			selectQuery.append(" and taxhead.isDebit = ?");
			preparedStatementValues.add( searchTaxHead.getIsDebit());
		}
		
		if (searchTaxHead.getValidFrom() != null && searchTaxHead.getValidTill()!=null) {
			selectQuery.append(" and taxhead.validfrom >= ?");
			preparedStatementValues.add(searchTaxHead.getValidFrom());
			selectQuery.append(" and taxhead.validtill <= ?");
			preparedStatementValues.add(searchTaxHead.getValidTill());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final TaxHeadMasterCriteria searchTaxHeads) {
		
		selectQuery.append(" ORDER BY name");

		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.commonsSearchPageSizeDefault());
		if (searchTaxHeads.getSize() != null)
			pageSize = searchTaxHeads.getSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (searchTaxHeads.getOffset() != null)
			pageNumber = searchTaxHeads.getOffset() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
	}
	
	public final String Update_Query = "UPDATE public.egbs_taxheadmaster SET  category=?, service=?, "
				+ "name=?, code=?, isdebit=?, isactualdemand=?, orderno=?, validfrom=?, validtill=?, "
				+ "lastmodifiedby=?, lastmodifiedtime=?"
				+ " WHERE tenantid=?";
	
	public final String Insert_Query = "INSERT INTO egbs_taxheadmaster(id, tenantid, category,"
				+ "service, name, code, isdebit,isactualdemand, orderno, validfrom, validtill, createdby, createdtime,"
				+ "lastmodifiedby, lastmodifiedtime) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static String getIdQuery(Set<String> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {

			String[] list = idList.toArray(new String[idList.size()]);
			query.append("'"+list[0]+"'");
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + "'"+list[i]+"'");
			}
		}
		return query.append(")").toString();
	}
}
