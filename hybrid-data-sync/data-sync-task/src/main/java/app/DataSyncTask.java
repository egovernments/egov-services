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
    
    @Value("${app.timezone}")
	private String timeZone;

    @Scheduled(fixedRateString = "${rate-in-milliseconds}")
    public void startSync() {
        Timestamp epoch = findEpoch();
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime timeZoneTime = ldt.atZone(ZoneId.of(timeZone));
        String timeZoneNow = timeZoneTime.format(dateFormatter);
        log.info("Staring sync at {} "+timeZone, timeZoneNow);

        for (String sourceSchema : sourceSchemas) {
            for (SyncInfo info : syncConfig.getInfo()) {
                Timestamp convertedEpoch = calculateEpochWithTZ(epoch, info);
                log.info(String.format("EPOCH: %s, table: %s, TZ: %s", convertedEpoch, info.getSourceTable(), info.getSourceTimeZone()));
                String query = String.format("SELECT %s from %s.%s WHERE lastmodifieddate >=? or createddate >=?",
                        String.join(",", info.getSourceColumnNamesToReadFrom()), sourceSchema, info.getSourceTable());
                log.info(query);
                jdbcTemplate.query(
                        query, new Object[]{convertedEpoch,convertedEpoch},
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
        updateEpoch(timeZoneNow);
    }

    private Timestamp calculateEpochWithTZ(Timestamp epoch, SyncInfo info) {
        ZonedDateTime timeZoneDateTime = ZonedDateTime.ofInstant(epoch.toInstant(), ZoneId.of(timeZone));
        LocalDateTime localDateTime = timeZoneDateTime.withZoneSameInstant(ZoneId.of(info.getSourceTimeZone())).toLocalDateTime();
        return Timestamp.valueOf(localDateTime);
    }

    private Timestamp findEpoch() {
        List<Map<String, Object>> res = jdbcTemplate.queryForList("SELECT epoch from data_sync_epoch where profile = ?", new Object[]{activeProfile});
        return (Timestamp) res.get(0).get("epoch");
    }

    private void updateEpoch(String epoch) {
        jdbcTemplate.update("UPDATE data_sync_epoch set epoch=? WHERE profile=?", new Object[]{Timestamp.valueOf(epoch), activeProfile});
    }
}
