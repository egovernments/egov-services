package org.egov.citizen.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.citizen.model.CommentResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.EqualsAndHashCode;

@Component
@EqualsAndHashCode
public class CommentsAndDocsRowMapper implements RowMapper<CommentResponse>  {
	
	@Override
	public CommentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		CommentResponse commentResponse = new CommentResponse();
		
		commentResponse.setSrn(rs.getString("srn"));
		commentResponse.setFrom(rs.getString("commentfrom"));
		commentResponse.setTimeStamp(rs.getLong("commentdate"));
		commentResponse.setText(rs.getString("comment"));
		commentResponse.setFilePath(rs.getString("filestoreid"));
		commentResponse.setDocFrom(rs.getString("uploadedby"));
		commentResponse.setDocTimeStamp(rs.getLong("uploaddate"));
		commentResponse.setUploadedbyrole(rs.getString("uploadedbyrole"));
		
		
		return commentResponse;
	}

}
