const kafka = require("kafka-node");
import envVariables from "../envVariables";
import producer from "./producer";
import get from "lodash/get";

const Consumer = kafka.Consumer;
let client;

if (process.env.NODE_ENV === "development") {
  client = new kafka.Client();
  console.log("local Consumer- ");
} else {
  client = new kafka.KafkaClient({ kafkaHost: envVariables.KAFKA_BROKER_HOST });
  console.log("cloud Consumer- ");
}

const consumer = new Consumer(
  client,
  [
    // { topic: "egov.collection.receipt-create", offset: 0 },
    { topic: "save-fn-firenoc", offset: 0,time: Date.now(),maxNum: 1 },
    { topic: "update-fn-firenoc", offset: 0,time: Date.now(),maxNum: 1 },
    { topic: "update-fn-workflow", offset: 0,time: Date.now(),maxNum: 1 }
  ],
  {
    autoCommit: false
  }
);

consumer.on("message", function(message) {
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

consumer.on("error", function(err) {
  console.log("Error:", err);
});

consumer.on("offsetOutOfRange", function(err) {
  console.log("offsetOutOfRange:", err);
});

export default consumer;
