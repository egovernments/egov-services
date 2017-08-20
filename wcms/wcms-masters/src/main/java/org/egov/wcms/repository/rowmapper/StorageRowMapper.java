package org.egov.wcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.wcms.model.StorageReservoir;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StorageRowMapper implements RowMapper<StorageReservoir> {

    @Override
    public StorageReservoir mapRow(ResultSet rs, int rowNum) throws SQLException {
        StorageReservoir storageReservoir = new StorageReservoir();
        storageReservoir.setId(rs.getLong("id"));
        return storageReservoir;
    }

}
