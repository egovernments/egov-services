package org.egov.infra.indexer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.egov.IndexerApplicationRunnerImpl;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.models.IndexJob;
import org.egov.infra.indexer.models.IndexJob.StatusEnum;
import org.egov.infra.indexer.models.IndexJobWrapper;
import org.egov.infra.indexer.producer.IndexerProducer;
import org.egov.infra.indexer.util.IndexerUtils;
import org.egov.infra.indexer.util.ResponseInfoFactory;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.Mapping.ConfigKeyEnum;
import org.egov.infra.indexer.web.contract.ReindexRequest;
import org.egov.infra.indexer.web.contract.ReindexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReindexService {

	@Autowired
	private BulkIndexer bulkIndexer;

	@Autowired
	private IndexerApplicationRunnerImpl runner;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private IndexerUtils indexerUtils;

	@Autowired
	private ResponseInfoFactory factory;

	@Autowired
	private IndexerProducer indexerProducer;

	@Autowired
	private IndexerService indexerService;

	@Value("${egov.core.reindex.topic.name}")
	private String reindexTopic;

	@Value("${egov.core.legacyindex.topic.name}")
	private String legacyIndexTopic;

	@Value("${egov.indexer.persister.create.topic}")
	private String persisterCreate;

	@Value("${egov.indexer.persister.update.topic}")
	private String persisterUpdate;

	@Value("${reindex.pagination.size.default}")
	private Integer defaultPageSizeForReindex;

	@Value("${legacyindex.pagination.size.default}")
	private Integer defaultPageSizeForLegacyindex;

	@Value("${egov.service.host}")
	private String serviceHost;

	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;

	@Value("${egov.core.no.of.index.threads}")
	private Integer noOfIndexThreads;

	@Value("${egov.core.index.thread.poll.ms}")
	private Long indexThreadPollInterval;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
	private final ScheduledExecutorService schedulerofChildThreads = Executors.newScheduledThreadPool(5);

	public ReindexResponse createReindexJob(ReindexRequest reindexRequest) {
		Map<String, Mapping> mappingsMap = runner.getMappingMaps();
		ReindexResponse reindexResponse = null;
		String uri = indexerUtils.getESSearchURL(reindexRequest);
		Object response = bulkIndexer.getESResponse(uri, null, null);
		Integer total = JsonPath.read(response, "$.hits.total");
		StringBuilder url = new StringBuilder();
		Index index = mappingsMap.get(reindexRequest.getReindexTopic()).getIndexes().get(0);
		url.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/_search");
		reindexResponse = ReindexResponse.builder().totalRecordsToBeIndexed(total)
				.estimatedTime(indexerUtils.fetchEstimatedTime(total))
				.message("Please hit the 'url' for the newly indexed data after the mentioned 'estimated time'.")
				.url(url.toString())
				.responseInfo(factory.createResponseInfoFromRequestInfo(reindexRequest.getRequestInfo(), true)).build();
		IndexJob job = IndexJob.builder().jobId(UUID.randomUUID().toString()).jobStatus(StatusEnum.INPROGRESS)
				.typeOfJob(ConfigKeyEnum.REINDEX).oldIndex(reindexRequest.getIndex() + "/" + reindexRequest.getType())
				.requesterId(reindexRequest.getRequestInfo().getUserInfo().getUuid())
				.newIndex(index.getName() + "/" + index.getType()).totalTimeTakenInMS(0L)
				.tenantId(reindexRequest.getTenantId()).recordsToBeIndexed(total).totalRecordsIndexed(0)
				.auditDetails(
						indexerUtils.getAuditDetails(reindexRequest.getRequestInfo().getUserInfo().getUuid(), true))
				.build();
		reindexRequest.setJobId(job.getJobId());
		reindexRequest.setStartTime(new Date().getTime());
		reindexRequest.setTotalRecords(total);
		IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(reindexRequest.getRequestInfo()).job(job)
				.build();
		indexerProducer.producer(reindexTopic, reindexRequest);
		indexerProducer.producer(persisterCreate, wrapper);
		reindexResponse.setJobId(job.getJobId());

		return reindexResponse;
	}

	public Boolean beginReindex(ReindexRequest reindexRequest) {
		indexThread(reindexRequest);
		return true;
	}

	private void indexThread(ReindexRequest reindexRequest) {
		final Runnable legacyIndexer = new Runnable() {
			boolean threadRun = true;

			public void run() {
				if (threadRun) {
					Boolean isProccessDone = false;
					increaseMaxResultWindow(reindexRequest, reindexRequest.getTotalRecords());
					String uri = indexerUtils.getESSearchURL(reindexRequest);
					ObjectMapper mapper = indexerUtils.getObjectMapper();
					Integer from = 0;
					Integer size = defaultPageSizeForReindex;
					while (!isProccessDone) {
						Object request = indexerUtils.getESSearchBody(from, size);
						Object response = bulkIndexer.getESResponse(uri, request, "POST");
						if (null != response) {
							List<Object> hits = JsonPath.read(response, "$.hits.hits");
							if (CollectionUtils.isEmpty(hits)) {
								isProccessDone = true;
								break;
							} else {
								List<Object> modifiedHits = new ArrayList<>();
								hits.parallelStream().forEach(hit -> {
									if (!isHitAMetaData(JsonPath.read(hit, "$._source"))) {
										modifiedHits.add(JsonPath.read(hit, "$._source"));
									}
								});
								Map<String, Object> requestToReindex = new HashMap<>();
								requestToReindex.put("hits", modifiedHits);
								childThreadExecutor(reindexRequest, mapper, reindexRequest);
								from += defaultPageSizeForReindex;
							}
						} else {
							IndexJob job = IndexJob.builder().jobId(reindexRequest.getJobId())
									.auditDetails(indexerUtils.getAuditDetails(
											reindexRequest.getRequestInfo().getUserInfo().getUuid(), false))
									.totalRecordsIndexed(from)
									.totalTimeTakenInMS(new Date().getTime() - reindexRequest.getStartTime())
									.jobStatus(StatusEnum.FAILED).build();
							IndexJobWrapper wrapper = IndexJobWrapper.builder()
									.requestInfo(reindexRequest.getRequestInfo()).job(job).build();
							indexerProducer.producer(persisterUpdate, wrapper);
							threadRun = false;
							log.info("Porcess failed! for data from: " + from + "and size: " + size);
							break;
						}
						IndexJob job = IndexJob.builder().jobId(reindexRequest.getJobId())
								.auditDetails(indexerUtils.getAuditDetails(
										reindexRequest.getRequestInfo().getUserInfo().getUuid(), false))
								.totalTimeTakenInMS(new Date().getTime() - reindexRequest.getStartTime())
								.jobStatus(StatusEnum.INPROGRESS).totalRecordsIndexed(from).build();
						IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(reindexRequest.getRequestInfo())
								.job(job).build();
						indexerProducer.producer(persisterUpdate, wrapper);
					}
					if (isProccessDone) {
						IndexJob job = IndexJob.builder().jobId(reindexRequest.getJobId())
								.auditDetails(indexerUtils.getAuditDetails(
										reindexRequest.getRequestInfo().getUserInfo().getUuid(), false))
								.totalRecordsIndexed(reindexRequest.getTotalRecords())
								.totalTimeTakenInMS(new Date().getTime() - reindexRequest.getStartTime())
								.jobStatus(StatusEnum.COMPLETED).build();
						IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(reindexRequest.getRequestInfo())
								.job(job).build();
						indexerProducer.producer(persisterUpdate, wrapper);
					}
				}
				threadRun = false;
			}
		};
		scheduler.schedule(legacyIndexer, indexThreadPollInterval, TimeUnit.MILLISECONDS);
	}

	public void childThreadExecutor(ReindexRequest reindexRequest, ObjectMapper mapper, Object requestToReindex) {
		final Runnable childThreadJob = new Runnable() {
			boolean threadRun = true;
			public void run() {
				if (threadRun) {
					try {
						indexerService.elasticIndexer(reindexRequest.getReindexTopic(),
								mapper.writeValueAsString(requestToReindex));
					} catch (Exception e) {
						threadRun = false;
					}
					threadRun = false;
				}
				threadRun = false;
				return;
			}
		};
		schedulerofChildThreads.scheduleAtFixedRate(childThreadJob, 0, indexThreadPollInterval - 10, TimeUnit.MILLISECONDS);

	}

	public boolean isHitAMetaData(Object hit) {
		ObjectMapper mapper = indexerUtils.getObjectMapper();
		boolean isMetaData = false;
		Map<String, Object> map = mapper.convertValue(hit, Map.class);
		Set<String> keySet = map.keySet();
		if (keySet.size() == 2) {
			if (keySet.contains("from") && keySet.contains("size"))
				isMetaData = true;
			else {
				isMetaData = false;
			}
		} else {
			isMetaData = false;
		}
		return isMetaData;
	}

	public void increaseMaxResultWindow(ReindexRequest reindexRequest, Integer totalRecords) {
		String uri = indexerUtils.getESSettingsURL(reindexRequest);
		Object body = indexerUtils.getESSettingsBody(totalRecords);
		Object response = bulkIndexer.getESResponse(uri, body, "PUT");
		if (response.toString().equals("OK")) {
			log.info("Max window set to " + (totalRecords + 50000) + " for index: " + reindexRequest.getIndex());
		}
	}

}
