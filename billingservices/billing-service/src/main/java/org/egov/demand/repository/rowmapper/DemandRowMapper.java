package org.egov.demand.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.Owner;
import org.egov.demand.model.enums.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemandRowMapper implements ResultSetExtractor<List<Demand>> {

	@Override
	public List<Demand> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, Demand> demandMap = new HashMap<>();
		String demandIdRsName = "did";

		while (rs.next()) {

			try {
				String demandId = (String) rs.getString(demandIdRsName);
				log.info("demandid in row mapper" + demandId);
				Demand demand = demandMap.get(demandId);

				if (demand == null) {

					demand = new Demand();
					demand.setId(demandId);
					demand.setBusinessService(rs.getString("dbusinessservice"));
					demand.setConsumerCode(rs.getString("dconsumerCode"));
					demand.setConsumerType(rs.getString("dconsumerType"));
					demand.setTaxPeriodFrom(rs.getLong("dtaxPeriodFrom"));
					demand.setTaxPeriodTo(rs.getLong("dtaxPeriodTo"));
					demand.setTenantId(rs.getString("dtenantid"));
					String type = rs.getString("dtype");
					demand.setType(Type.fromValue(type));

					Double minimumAmountPayable = rs.getDouble("dminimumAmountPayable");
					if (minimumAmountPayable == 0)
						demand.setMinimumAmountPayable(null);
					else
						demand.setMinimumAmountPayable(minimumAmountPayable);

					Owner owner = new Owner();
					owner.setId(rs.getLong("downer"));
					demand.setOwner(owner);

					AuditDetail auditDetail = new AuditDetail();
					auditDetail.setCreatedBy(rs.getString("dcreatedby"));
					auditDetail.setLastModifiedBy(rs.getString("dlastModifiedby"));
					auditDetail.setCreatedTime(rs.getLong("dcreatedtime"));
					auditDetail.setLastModifiedTime(rs.getLong("dlastModifiedtime"));
					demand.setAuditDetail(auditDetail);

					demand.setDemandDetails(new ArrayList<>());
					demandMap.put(demand.getId(),demand);
				}

				DemandDetail demandDetail = new DemandDetail();
				demandDetail.setId(rs.getString("dlid"));
				demandDetail.setDemandId(rs.getString("dldemandid"));
				demandDetail.setTaxHeadCode(rs.getString("dltaxheadcode"));
				demandDetail.setTenantId(rs.getString("dltenantid"));

				Double dlTaxAmount = rs.getDouble("dltaxamount");
				Double dlcollectionamount = rs.getDouble("dlcollectionamount");
				if (dlTaxAmount == 0)
					demandDetail.setTaxAmount(null);
				else
					demandDetail.setTaxAmount(dlTaxAmount);
				if (dlcollectionamount == 0)
					demandDetail.setCollectionAmount(null);
				else
					demandDetail.setCollectionAmount(dlcollectionamount);

				AuditDetail dlauditDetail = new AuditDetail();
				dlauditDetail.setCreatedBy(rs.getString("dlcreatedby"));
				dlauditDetail.setCreatedTime(rs.getLong("dlcreatedtime"));
				dlauditDetail.setLastModifiedBy(rs.getString("dllastModifiedby"));
				dlauditDetail.setLastModifiedTime(rs.getLong("dllastModifiedtime"));
				demandDetail.setAuditDetail(dlauditDetail);

				if (demand.getId().equals(demandDetail.getDemandId()))
					demand.getDemandDetails().add(demandDetail);
			} catch (Exception e) {
				log.info("exception in demandRowMapper : " + e);
				throw new RuntimeException("error while mapping object from reult set : " + e);
			}
		}
		log.info("converting map to list object ::: " + demandMap.values());
		return new ArrayList<>(demandMap.values());
	}
}
