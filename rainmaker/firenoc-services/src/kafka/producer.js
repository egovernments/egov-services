var kafka = require("kafka-node");

const Producer = kafka.Producer;
let client = new kafka.KafkaClient({ kafkaHost: process.env.KAFKA_BROKER_HOST });

const producer = new Producer(client);

producer.on("ready", function() {
  console.log("Producer is ready");
});

producer.on("error", function(err) {
  console.log("Producer is in error state");
  console.log(err);
});

export default producer;
