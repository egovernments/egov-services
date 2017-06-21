package org.egov.property.repository.builder;

public class UnitBuilder {

	public static final String INSERT_UNIT_QUERY = "INSERT INTO egpt_unit ("
			+ "unitNo, unitType, length,width,builtupArea,assessableArea,"
			+ "bpaBuiltupArea,bpaNo,bpaDate,usage,occupancy,occupierName,firmName,rentCollected, structure, age,"
			+ "exemptionReason, isStructured, occupancyDate, constCompletionDate, manualArv, arv,"
			+ "electricMeterNo, waterMeterNo, createdBy, lastModifiedBy, createdTime, lastModifiedTime,"
			+ "floor) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String updateUnitQuery() {

		StringBuffer unitSql = new StringBuffer();

		unitSql.append("UPDATE egpt_unit").append(" SET unitNo = ?, unitType = ?, length = ?, width = ?,")
				.append(" builtupArea = ?, assessableArea = ?, bpaBuiltupArea = ?,")
				.append(" bpaNo = ?, bpaDate = ?, usage = ?, occupancy = ?, occupierName = ?,")
				.append(" firmName = ?, rentCollected = ?, structure = ?, age = ?,")
				.append(" exemptionReason = ?, isStructured = ?, occupancyDate = ?,")
				.append(" constCompletionDate = ?, manualArv = ?, arv = ?,")
				.append(" electricMeterNo = ?, waterMeterNo = ?, lastModifiedBy = ?, lastModifiedTime = ?")
				.append(" WHERE id = ");

		return unitSql.toString();
	}

}
