package org.egov.lcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocateDetails;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AdvocateDetailsRowMapper implements RowMapper<AdvocateDetails> {
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public AdvocateDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

		AdvocateDetails advocateDetails = new AdvocateDetails();
		advocateDetails.setCode(rs.getString("code"));
		advocateDetails.setAssignedDate(getLong(rs.getLong("assigndate")));
		advocateDetails.setFee(getDouble(rs.getDouble("fee")));

		TypeReference<Advocate> advocateTypeRef = new TypeReference<Advocate>() {
		};

		try {
			if (rs.getString("advocate") != null)
				advocateDetails.setAdvocate(objectMapper.readValue(rs.getString("advocate"), advocateTypeRef));

		} catch (Exception ex) {
			throw new CustomException(propertiesManager.getJsonStringError(), ex.getMessage());
		}

		return advocateDetails;
	}

	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

	private Double getDouble(Object object) {
		return object == null ? 0 : Double.parseDouble(object.toString());
	}
}
