package application;

import connector.ElasticsearchConnector;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import processor.SessionProcessor;
import scala.Tuple2;

import java.util.Map;

@Slf4j
public class BatchApplication {

    public static void executeBatch(Long startTime, Long endTime) throws Exception {
        ElasticsearchConnector elasticsearchConnector = new ElasticsearchConnector();

        JavaPairRDD<String, Map<String, Object>> esRDD = elasticsearchConnector.getTelemetryRecords(startTime, endTime);

        System.out.println("Total Records read from ES: " + esRDD.count());

        SessionProcessor.init();

        JavaPairRDD<Object, Iterable<Tuple2<String, Map<String, Object>>>> deviceGroup = esRDD.groupBy(new Function<Tuple2<String,Map<String,Object>>, Object>() {
            @Override
            public Object call(Tuple2<String, Map<String, Object>> v1) throws Exception {
                return ( (Map<String, Object>) v1._2.get("context")).get("did");
            }
        });

        System.out.println("Unique Devices: " + deviceGroup.count());

        deviceGroup.foreach(new VoidFunction<Tuple2<Object, Iterable<Tuple2<String, Map<String, Object>>>>>() {
            @Override
            public void call(Tuple2<Object, Iterable<Tuple2<String, Map<String, Object>>>> objectIterableTuple2)
                    throws Exception {
                SessionProcessor.processRecords(objectIterableTuple2._2.iterator());
            }
        });

        System.out.println("Total Sessions: " + SessionProcessor.totalSessionCounter);

    }

}
