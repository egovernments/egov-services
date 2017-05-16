package org.egov.egf.producer;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.egov.egf.utils.Utils;

public class FinancialCustomPatitioner implements Partitioner {

	public FinancialCustomPatitioner() {
	}

	@Override
	public void configure(Map<String, ?> configs) {

	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

		Integer partition = Utils.KEY_PARTITION_MAP.get(key.toString());
		
		if (partition != null)
			return partition;
		else
			return 0;
	}

	@Override
	public void close() {

	}

}