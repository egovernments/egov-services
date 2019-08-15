const kafka = require("kafka-node");
import envVariables from "../envVariables";
import producer from "./producer";
import get from "lodash/get";
var options = {
  // connect directly to kafka broker (instantiates a KafkaClient)
  kafkaHost: envVariables.KAFKA_BROKER_HOST,
  groupId: "firenoc-consumer-grp",
  autoCommit: true,
  autoCommitIntervalMs: 5000,
  sessionTimeout: 15000,
  fetchMaxBytes: 10 * 1024 * 1024, // 10 MB
  // An array of partition assignment protocols ordered by preference. 'roundrobin' or 'range' string for
  // built ins (see below to pass in custom assignment protocol)
  protocol: ["roundrobin"],
  // Offsets to use for new groups other options could be 'earliest' or 'none'
  // (none will emit an error if no offsets were saved) equivalent to Java client's auto.offset.reset
  fromOffset: "latest",
  // how to recover from OutOfRangeOffset error (where save offset is past server retention)
  // accepts same value as fromOffset
  outOfRangeOffset: "earliest"
};

var consumerGroup = new kafka.ConsumerGroup(options, [
  "save-fn-firenoc",
  "update-fn-firenoc",
  "update-fn-workflow",
  "egov.collection.receipt-create"
]);

console.log("Consumer ");

consumerGroup.on("message", function(message) {
  console.log("consumer-topic", message.topic);
  console.log("consumer-value", JSON.parse(message.value));
  const value = JSON.parse(message.value);

  let payloads = [];
  const topic = "egov.core.notification.sms";
  let smsRequest = {};

  const getInitializedSMSRequest = (firenoc = {}) => {
    return {
      mobileNumber: get(firenoc, "fireNOCDetails.owners.0.mobileNumber"),
      message: `Dear ${get(
        firenoc,
        "fireNOCDetails.owners.0.name"
      )},Your application for ${get(
        firenoc,
        "fireNOCDetails.fireNOCType"
      )} has been generated. Your application no. is ${get(
        firenoc,
        "fireNOCDetails.applicationNumber"
      )}.`
    };
  };

  switch (message.topic) {
    case envVariables.KAFKA_TOPICS_FIRENOC_CREATE:
      {
        const { FireNOCs } = value;
        for (let i = 0; i < FireNOCs.length; i++) {
          smsRequest = getInitializedSMSRequest(FireNOCs[i]);
          payloads.push({
            topic,
            messages: JSON.stringify(smsRequest)
          });
        }
      }
      break;
    case envVariables.KAFKA_TOPICS_FIRENOC_UPDATE:
      {
        const { FireNOCs } = value;
        for (let i = 0; i < FireNOCs.length; i++) {
          if (FireNOCs[i].fireNOCDetails.status == "INITIATED") {
            smsRequest = getInitializedSMSRequest(FireNOCs[i]);
          } else {
            smsRequest = {
              mobileNumber: get(
                FireNOCs[i],
                "fireNOCDetails.owners.0.mobileNumber"
              ),
              message: `Dear ${get(
                FireNOCs[i],
                "fireNOCDetails.owners.0.name"
              )},
                  Your application for ${get(
                    FireNOCs[i],
                    "fireNOCDetails.fireNOCType"
                  )} has been submitted. Your application no. is ${get(
                FireNOCs[i],
                "fireNOCDetails.fireNOCType"
              )}. Please pay your NoC Fees online or at your applicable fire office`
            };
          }
          payloads.push({
            topic,
            messages: JSON.stringify(smsRequest)
          });
        }
      }
      break;
    case envVariables.KAFKA_TOPICS_FIRENOC_WORKFLOW:
      {
        console.log("workflow hit");
      }
      break;
  }

  producer.send(payloads, function(err, data) {
    if (!err) {
      console.log(data);
    } else {
      console.log(err);
    }
  });
});

consumerGroup.on("error", function(err) {
  console.log("Error:", err);
});

consumerGroup.on("offsetOutOfRange", function(err) {
  console.log("offsetOutOfRange:", err);
});

export default consumerGroup;
