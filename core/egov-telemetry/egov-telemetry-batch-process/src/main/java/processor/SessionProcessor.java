package processor;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import config.AppProperties;
import connector.ElasticsearchConnector;
import constants.TelemetryConstants;
import lombok.extern.slf4j.Slf4j;
import models.Session;
import models.SessionDetails;
import org.json.JSONObject;
import producer.Producer;
import scala.Tuple2;
import util.SessionIterator;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class SessionProcessor {

    private static ObjectMapper mapper;
    private static String kafkaTopic;
    private static AppProperties appProperties;
    private static Producer producer;
    private static Configuration configuration;

    public static Integer totalSessionCounter;

    private static List<String> existingUserIds;

    private static ElasticsearchConnector elasticsearchConnector;

    public SessionProcessor() {
        init();
    }

    public static void init() {
        appProperties = new AppProperties();
        mapper = new ObjectMapper();
        producer = new Producer();
        kafkaTopic = appProperties.getOutputKafkaTopic();

        configuration = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS);

        elasticsearchConnector = new ElasticsearchConnector();
        existingUserIds = elasticsearchConnector.getExistingUserIds();

        totalSessionCounter = 0;
    }


    public static void processRecords(Iterator<Tuple2<String, Map<String, Object>>> deviceReocrds) {
        SessionIterator sessionIterator = new SessionIterator(deviceReocrds);
        Session session;
        while (sessionIterator.hasNext()) {
            session = buildSession(sessionIterator.next());
//            printSession(session);
            pushSession(session);
            totalSessionCounter++;
        }
    }

    private static Session buildSession(List<Map<String, Object>> sessionContent) {

        String sessionIid = UUID.randomUUID().toString();
        String deviceId = JsonPath.using(configuration).parse(new JSONObject(sessionContent.get(0)).toString())
                .read("$.context.did");

        Long startTime = JsonPath.using(configuration).parse(new JSONObject(sessionContent.get(0)).toString())
                .read("$.ets");
        Long endTime = JsonPath.using(configuration).parse(new
                JSONObject(sessionContent.get(sessionContent.size() - 1)).toString()).read("$.ets");
        String timestamp = getTimestamp(startTime);

        Long duration = endTime - startTime;

        String exitPage = JsonPath.using(configuration).parse(new
                JSONObject(sessionContent.get(sessionContent.size() - 1)).toString()).read("$.edata.url");

        String userId = getUserId(sessionContent);
        String userType = getUserType(sessionContent);


        boolean isNewUser = false;

        if(existingUserIds.indexOf(userId) == -1) {
            isNewUser = true;
            existingUserIds.add(userId);
        }

        Integer pageCount = getPageCount(sessionContent);

        SessionDetails sessionDetails = SessionDetails.builder().pageCount(pageCount).duration(duration)
                .exitPage(exitPage).userType(userType).build();
        Session session = Session.builder().sessionId(sessionIid).timestamp(timestamp).deviceId(deviceId).userId(userId)
                .isNewUser(isNewUser).startTime(startTime).endTime(endTime).sessionDetails(sessionDetails).build();

        return session;
    }

    private static Integer getPageCount(List<Map<String, Object>> sessionContent) {
        Integer pageCount = 0;

        for (Map<String, Object> record : sessionContent) {
            if("SUMMARY".equalsIgnoreCase(JsonPath.using(configuration).parse(new JSONObject(record).toString()).read
                    ("$.eid"))) {
                pageCount++;
            }
        }

        return pageCount;
    }

    private static String getTimestamp(Long startTime) {
        Date date = new Date(Long.valueOf(startTime));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    private static String getUserId(List<Map<String, Object>> sessionContent) {
        for (Map<String, Object> record :sessionContent) {
            String userId = JsonPath.using(configuration).parse(new JSONObject(record).toString()).read("$.actor.id");
            if(userId == null)
                continue;
            if(! userId.equalsIgnoreCase(TelemetryConstants.userNotFoundIdentifier))
                return userId;
        }
        return TelemetryConstants.userNotFoundIdentifier;
    }


    private static String getUserType(List<Map<String, Object>> sessionContent) {
        for (Map<String, Object> record : sessionContent) {
            String url = JsonPath.using(configuration).parse(new JSONObject(record).toString()).read("$.edata.url");
            if(url == null)
                continue;
            if(url.contains("citizen"))
                return TelemetryConstants.citizenUserType;
            if(url.contains("employee"))
                return TelemetryConstants.employeeUserType;
        }
        return null;
    }


    public static void printSession(Session session) {
        System.out.println(session.getSessionId() + ", " + session.getSessionDetails().getDuration() + ", " + session
                .getSessionDetails().getExitPage());
    }

    public static void pushSession(Session session) {
        JsonNode jsonNode = mapper.valueToTree(session);
        producer.push(kafkaTopic, session.getSessionId(), session.getStartTime(), jsonNode);
    }
}
