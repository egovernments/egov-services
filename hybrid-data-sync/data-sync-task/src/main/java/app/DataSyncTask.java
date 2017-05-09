package app;

import app.config.SyncConfig;
import app.config.SyncInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class DataSyncTask {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SyncConfig syncConfig;

    private static final Logger log = LoggerFactory.getLogger(DataSyncTask.class);

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("#{'${source-schemas}'.split(',')}")
    private List<String> sourceSchemas;

    @Value("${destination-schema}")
    private String destinationSchema;

    @Value("${state}")
    private String state;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Scheduled(fixedRateString = "${rateInSeconds}")
    public void startSync() {
        Timestamp epoch = findEpoch();
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime istTime = ldt.atZone(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime utcTime = ldt.atZone(ZoneId.of("UTC"));
        String istNow = istTime.format(dateFormatter);
        String utcNow = utcTime.format(dateFormatter);
        log.info("Staring sync at {}", istNow);

        for (String sourceSchema : sourceSchemas) {
            epoch = calculateEpochWithTZ(epoch, sourceSchema);
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
        updateEpoch(utcNow);
    }

    private Timestamp calculateEpochWithTZ(Timestamp epoch, String sourceSchema) {
        if (Objects.equals(sourceSchema, "microservice")) {
            ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(epoch.toInstant(), ZoneId.of("UTC"));
            LocalDateTime localDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata")).toLocalDateTime();
            return Timestamp.valueOf(localDateTime);
        } else {
            return epoch;
        }
    }

    private Timestamp findEpoch() {
        List<Map<String, Object>> res = jdbcTemplate.queryForList("SELECT epoch from data_sync_epoch where profile = ?", new Object[]{activeProfile});
        return (Timestamp) res.get(0).get("epoch");
    }

    private void updateEpoch(String epoch) {
        jdbcTemplate.update("UPDATE data_sync_epoch set epoch=? WHERE profile=?", new Object[]{Timestamp.valueOf(epoch), activeProfile});
    }
}
