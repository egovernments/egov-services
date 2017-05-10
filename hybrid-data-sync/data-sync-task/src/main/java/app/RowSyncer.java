package app;

import app.config.SyncInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RowSyncer {
    private SyncInfo syncInfo;
    private CustomResultSet rs;
    private String sourceSchema;
    private String destinationSchema;
    private String state;

    private JdbcTemplate jdbcTemplate;

    private RowColumnValueMapper columnValueMapper;

    private static final Logger log = LoggerFactory.getLogger(RowSyncer.class);

    public RowSyncer(SyncInfo syncInfo, CustomResultSet rs, String sourceSchema, String destinationSchema, String state, JdbcTemplate jdbcTemplate) {
        this.syncInfo = syncInfo;
        this.rs = rs;
        this.sourceSchema = sourceSchema;
        this.destinationSchema = getDestinationSchema(destinationSchema);
        this.state = state;
        this.jdbcTemplate = jdbcTemplate;
        columnValueMapper = new RowColumnValueMapper(syncInfo, rs, jdbcTemplate);
    }

    private String getDestinationSchema(String destinationSchema) {
        if (Objects.equals(sourceSchema, "microservice")) {
            return String.format("%s", rs.get("tenantid")).split("\\.")[1];
        }
        return destinationSchema;
    }

    public void insertOrUpdate() throws SQLException {
        try {
            insert();
        } catch (DuplicateKeyException e) {
            update();
        }
    }

    private void insert() throws SQLException {
        List<String> destinationColumns = columnValueMapper.getCommaSeparatedColumnNames();
        List<String> destinationColumnValues = columnValueMapper.getCommaSeparatedColumnValues();
        if (Objects.equals(destinationSchema, "microservice")) {
            destinationColumns.add("tenantId");
            destinationColumnValues.add(String.format("'%s.%s'", state, sourceSchema));
        }

        String insertQuery = String.format("INSERT INTO %s.%s (%s) VALUES (%s)",
                destinationSchema,
                syncInfo.getDestinationTable(),
                String.join(",", destinationColumns),
                String.join(",", destinationColumnValues)
        );
        log.info("Trying to insert");
        log.info(insertQuery);
        jdbcTemplate.update(insertQuery);
    }

    private void update() throws SQLException {
        log.info("Insert failed");
        log.info("Trying to update");
        String updateQuery = String.format("UPDATE %s.%s set %s WHERE id = '%s'",
                destinationSchema,
                syncInfo.getDestinationTable(),
                String.join(",", columnValueMapper.getColumnNameValuePairForUpdate()),
                rs.get("id")
        );

        if (Objects.equals(destinationSchema, "microservice")) {
            updateQuery = String.format("%s AND tenantId = '%s'",
                    updateQuery,
                    String.format("%s.%s", state, sourceSchema));
        }

        log.info(updateQuery);
        jdbcTemplate.update(updateQuery);
    }
}
