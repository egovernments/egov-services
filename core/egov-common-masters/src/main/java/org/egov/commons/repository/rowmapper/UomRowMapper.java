package org.egov.commons.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.commons.model.Uom;
import org.egov.commons.model.UomCategory;
import org.springframework.jdbc.core.RowMapper;

public class UomRowMapper implements RowMapper<Uom> {

	@Override
	public Uom mapRow(ResultSet rs, int arg1) throws SQLException {
		Uom uom = new Uom();
		
		uom.setId(rs.getLong("id"));
		uom.setBaseuom((Boolean) rs.getObject("baseuom"));

		UomCategory category=new UomCategory();
		category.setId(rs.getLong("uomcategoryid"));
		uom.setCategory(category);
		
		uom.setConvFactor((Long) rs.getObject("conv_factor"));
		uom.setNarration(rs.getString("narration"));
		// TODO : tanantId has to be included
		// uom.setTenantId(rs.getString(""));

		uom.setUom(rs.getString("uom"));
		uom.setVersion((Integer) rs.getObject("version"));
		
		return uom;
	}

}
