const kafka = require("kafka-node");
import envVariables from "../envVariables";
import producer from "./producer";
import get from "lodash/get";
// import { httpRequest } from "../api";

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
  envVariables.KAFKA_TOPICS_FIRENOC_CREATE,
  envVariables.KAFKA_TOPICS_FIRENOC_UPDATE,
  envVariables.KAFKA_TOPICS_FIRENOC_WORKFLOW,
  envVariables.KAFKA_TOPICS_RECEIPT_CREATE
]);

console.log("Consumer ");

consumerGroup.on("message", function(message) {
  console.log("consumer-topic", message.topic);
  console.log("consumer-value", JSON.parse(message.value));
  const value = JSON.parse(message.value);

  let payloads = [];
  const topic = envVariables.KAFKA_TOPICS_NOTIFICATION;
  let smsRequest = {};
  let events = [];
  let { RequestInfo } = value;

  const sendEventNotificaiton = () => {
    let requestPayload = {
      // RequestInfo,
      events
    };

    payloads.push({
      topic:envVariables.KAFKA_TOPICS_EVENT_NOTIFICATION,
      messages: JSON.stringify(requestPayload)
    });
    // httpRequest({
    //   hostURL: envVariables.EGOV_EVENT_HOST,
    //   endPoint: `${envVariables.EGOV_EVENT_CONTEXT_PATH}${envVariables.EGOV_EVENT_CREATE_ENPOINT}`,
    //   requestPayload
    // }).then(
    //   function(response) {
    //     console.log(response);
    //   },
    //   function(error) {
    //     console.log(error);
    //   }
    // );
  };

  const sendFireNOCSMSRequest = FireNOCs => {
    for (let i = 0; i < FireNOCs.length; i++) {
      smsRequest["mobileNumber"] = get(
        FireNOCs[i],
        "fireNOCDetails.applicantDetails.owners.0.mobileNumber"
      );
      let firenocType =
        get(FireNOCs[i], "fireNOCDetails.fireNOCType") === "NEW"
          ? "new"
          : "provision";

      let ownerName = get(
        FireNOCs[i],
        "fireNOCDetails.applicantDetails.owners.0.name"
      );
      let uuid = get(
        FireNOCs[i],
        "fireNOCDetails.applicantDetails.owners.0.uuid"
      );
      let applicationNumber = get(
        FireNOCs[i],
        "fireNOCDetails.applicationNumber"
      );
      let fireNOCNumber = get(FireNOCs[i], "fireNOCDetails.validTo");
      let validTo = get(FireNOCs[i], "fireNOCDetails.validTo");
      let tenantId = get(FireNOCs[i], "tenantId");
      switch (FireNOCs[i].fireNOCDetails.status) {
        case "INITIATED":
          smsRequest = smsRequest[
            "message"
          ] = `Dear ${ownerName},Your application for ${firenocType} has been generated. Your application no. is ${applicationNumber}.`;
          break;
        case "PENDINGPAYMENT":
          smsRequest[
            "message"
          ] = `Dear ${ownerName},Your application for ${firenocType} has been submitted. Your application no. is ${applicationNumber}. Please pay your NoC Fees online or at your applicable fire office`;
          break;
        case "DOCUMENTVERIFY":
          smsRequest[
            "message"
          ] = `Dear ${ownerName},Your application for ${firenocType} with application no. is ${applicationNumber} has been forwarded for field inpsection.`;
          break;
        case "FIELDINSPECTION":
          smsRequest[
            "message"
          ] = `Dear ${ownerName},Your application for ${firenocType} with application no. is ${applicationNumber} has been forwarded for document verifier.`;
          break;
        case "PENDINGAPPROVAL":
          smsRequest[
            "message"
          ] = `Dear ${ownerName},Your application for ${firenocType} with application no. is ${applicationNumber} has been forwarded for approver.`;
          break;
        case "APPROVED":
          var currentDate = new Date(validTo);
          var date = currentDate.getDate();
          var month = currentDate.getMonth(); //Be careful! January is 0 not 1
          var year = currentDate.getFullYear();

          var dateString =
            date +
            "-" +
            (month + 1 > 9 ? month + 1 : `0${month + 1}`) +
            "-" +
            year;

          smsRequest[
            "message"
          ] = `Dear ${ownerName},Your application for ${firenocType} with application no. is ${applicationNumber} is approved.And your fire NoC has been generated.Your Fire NoC No. is ${fireNOCNumber}. It is valid till ${dateString}`;
          break;
        case "REJECTED":
          smsRequest[
            "message"
          ] = `Dear ${ownerName},Your application for ${firenocType} with application no. is ${applicationNumber} has been rejected.To know more details please contact your applicable fire office`;
          break;
        // case "CANCELLED":
        //   break;
        default:
      }
      payloads.push({
        topic,
        messages: JSON.stringify(smsRequest)
      });
      if (smsRequest.message) {
        events.push({
          tenantId: tenantId,
          eventType: "SYSTEMGENERATED",
          description: smsRequest.message,
          name: "Firenoc notification",
          source: "webapp",
          recepient: {
            toUsers: [uuid]
          }
        });
      }
    }
    if (events.length > 0) {
      sendEventNotificaiton();
    }
  };

  switch (message.topic) {
    case envVariables.KAFKA_TOPICS_FIRENOC_CREATE:
      {
        const { FireNOCs } = value;
        sendFireNOCSMSRequest(FireNOCs);
      }
      break;
    case envVariables.KAFKA_TOPICS_FIRENOC_UPDATE:
      {
        const { FireNOCs } = value;
        sendFireNOCSMSRequest(FireNOCs);
      }
      break;
    case envVariables.KAFKA_TOPICS_FIRENOC_WORKFLOW:
      {
        const { FireNOCs } = value;
        sendFireNOCSMSRequest(FireNOCs);
      }
      break;

    case envVariables.KAFKA_TOPICS_RECEIPT_CREATE:
      {
        console.log("reciept hit");
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
