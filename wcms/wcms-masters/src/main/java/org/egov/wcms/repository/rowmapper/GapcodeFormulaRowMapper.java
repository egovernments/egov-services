package org.egov.wcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.wcms.model.CommonDataModel;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class GapcodeFormulaRowMapper implements RowMapper<CommonDataModel> {
	  @Override
	    public CommonDataModel mapRow(final ResultSet rs, final int rowNum) throws SQLException {
	        final CommonDataModel  commonDataModel = new CommonDataModel();
	        commonDataModel.setKey(String.valueOf(rs.getString("code")));
	        commonDataModel.setObject(rs.getString("name"));	        
	        return commonDataModel;
	    }
}
