package org.egov.eis.repository.helper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.stereotype.Component;

@Component
public class PreparedStatementHelper {

	public void setIntegerOrNull(PreparedStatement ps, int index, Integer value) {
		if (value == null) {
			try {
				ps.setNull(index, Types.INTEGER);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ps.setInt(index, value);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void setLongOrNull(PreparedStatement ps, int index, Long value) {
		if (value == null) {
			try {
				ps.setNull(index, Types.BIGINT);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ps.setLong(index, value);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}