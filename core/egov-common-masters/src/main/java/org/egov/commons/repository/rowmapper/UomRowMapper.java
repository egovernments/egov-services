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
		uom.setBaseuom(rs.getBoolean("baseuom"));
		
		UomCategory category=new UomCategory();
		category.setId(rs.getLong("uomcategoryid"));
		uom.setCategory(category);
		
		uom.setConvFactor(rs.getLong("conv_factor"));
		uom.setNarration(rs.getString("narration"));
		//Todo tanantId has to include
		//uom.setTenantId(rs.getString(""));
		
		uom.setUom(rs.getString("uom"));
		uom.setVersion(rs.getInt("version"));
		
		return uom;
	}

}
