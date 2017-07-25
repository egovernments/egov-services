package org.egov.property.repository.builder;

public class UnitBuilder {

	public static final String INSERT_UNIT_QUERY = "INSERT INTO egpt_unit ("
			+ "unitNo, unitType, length,width,builtupArea,assessableArea,"
			+ "bpaBuiltupArea,bpaNo,bpaDate,usage,occupancy,occupierName,firmName,rentCollected, structure, age,"
			+ "exemptionReason, isStructured, occupancyDate, constCompletionDate, manualArv, arv,"
			+ "electricMeterNo, waterMeterNo, createdBy, lastModifiedBy, createdTime, lastModifiedTime,"
			+ "floor,isAuthorised) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String updateUnitQuery() {

		StringBuffer unitSql = new StringBuffer();

		unitSql.append("UPDATE egpt_unit").append(" SET unitNo = ?, unitType = ?, length = ?, width = ?,")
				.append(" builtupArea = ?, assessableArea = ?, bpaBuiltupArea = ?,")
				.append(" bpaNo = ?, bpaDate = ?, usage = ?, occupancy = ?, occupierName = ?,")
				.append(" firmName = ?, rentCollected = ?, structure = ?, age = ?,")
				.append(" exemptionReason = ?, isStructured = ?, occupancyDate = ?,")
				.append(" constCompletionDate = ?, manualArv = ?, arv = ?,")
				.append(" electricMeterNo = ?, waterMeterNo = ?, lastModifiedBy = ?, lastModifiedTime = ?,")
				.append(" parentid = ?,isAuthorised = ? WHERE id = ?");

		return unitSql.toString();
	}

	public static final String UNITS_BY_FLOOR_QUERY = "select * from egpt_unit where floor = ?";

	public static final String INSERT_ROOM_QUERY = "INSERT INTO egpt_unit ("
			+ "unitNo, unitType, length,width,builtupArea,assessableArea,"
			+ "bpaBuiltupArea,bpaNo,bpaDate,usage,occupancy,occupierName,firmName,rentCollected, structure, age,"
			+ "exemptionReason, isStructured, occupancyDate, constCompletionDate, manualArv, arv,"
			+ "electricMeterNo, waterMeterNo, createdBy, lastModifiedBy, createdTime, lastModifiedTime,"
			+ "floor,parentid,isAuthorised) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String updateRoomQuery() {

		StringBuffer unitSql = new StringBuffer();

		unitSql.append("UPDATE egpt_unit").append(" SET unitNo = ?, unitType = ?, length = ?, width = ?,")
				.append(" builtupArea = ?, assessableArea = ?, bpaBuiltupArea = ?,")
				.append(" bpaNo = ?, bpaDate = ?, usage = ?, occupancy = ?, occupierName = ?,")
				.append(" firmName = ?, rentCollected = ?, structure = ?, age = ?,")
				.append(" exemptionReason = ?, isStructured = ?, occupancyDate = ?,")
				.append(" constCompletionDate = ?, manualArv = ?, arv = ?,")
				.append(" electricMeterNo = ?, waterMeterNo = ?, lastModifiedBy = ?, lastModifiedTime = ?,")
				.append("  parentid = ?, isAuthorised = ? WHERE id = ?");

		return unitSql.toString();
	}

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_unit where id= ?";

	public static final String INSERT_UNITHISTORY_QUERY = "INSERT INTO egpt_unit_history ("
			+ "unitNo, unitType, length,width,builtupArea,assessableArea,"
			+ "bpaBuiltupArea,bpaNo,bpaDate,usage,occupancy,occupierName,firmName,rentCollected, structure, age,"
			+ "exemptionReason, isStructured, occupancyDate, constCompletionDate, manualArv, arv,"
			+ "electricMeterNo, waterMeterNo, createdBy, lastModifiedBy, createdTime, lastModifiedTime,"
			+ "floor,isAuthorised,id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_ROOMHISTORY_QUERY = "INSERT INTO egpt_unit_history ("
			+ "unitNo, unitType, length,width,builtupArea,assessableArea,"
			+ "bpaBuiltupArea,bpaNo,bpaDate,usage,occupancy,occupierName,firmName,rentCollected, structure, age,"
			+ "exemptionReason, isStructured, occupancyDate, constCompletionDate, manualArv, arv,"
			+ "electricMeterNo, waterMeterNo, createdBy, lastModifiedBy, createdTime, lastModifiedTime,"
			+ "floor,parentid,isAuthorised,id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

}
