package app;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import app.config.*;

@Component
public class DataSyncTask {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SyncConfig syncConfig;

    private static final Logger log = LoggerFactory.getLogger(DataSyncTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("#{'${source-schemas}'.split(',')}")
    private List<String> sourceSchemas;

    @Value("${destination-schema}")
    private String destinationSchema;

    @Value("${state}")
    private String state;

    @Scheduled(fixedRateString = "${rateInSeconds}")
    public void startSync() {
        Timestamp epoch = findEpoch();
        String now = dateFormat.format(new Date());
        log.info("Staring sync at {}", now);

        for (String sourceSchema : sourceSchemas) {
            for (SyncInfo info : syncConfig.getInfo()) {
                String query = String.format("SELECT %s from %s.%s WHERE lastmodifieddate >=?",
                        String.join(",", info.getSourceColumnNamesToReadFrom()), sourceSchema, info.getSourceTable());
                log.info(query);
                jdbcTemplate.query(
                        query, new Object[]{epoch},
                        (rs, rowNum) -> new CustomResultSet(rs, info.getSourceColumnConfigsToReadFrom())
                ).forEach(res -> {
                    try {
                        new RowSyncer(info, res, sourceSchema, destinationSchema, state, jdbcTemplate).insertOrUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        updateEpoch(now);
    }

    private Timestamp findEpoch() {
        List<Map<String, Object>> res = jdbcTemplate.queryForList("SELECT epoch from data_sync_epoch LIMIT 1");
        return (Timestamp) res.get(0).get("epoch");
    }

    private void updateEpoch(String epoch) {
        jdbcTemplate.update("UPDATE data_sync_epoch set epoch=?", new Object[]{Timestamp.valueOf(epoch)});
    }
}
