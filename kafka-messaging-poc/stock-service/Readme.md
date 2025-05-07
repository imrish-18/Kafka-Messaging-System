download kafka
run the below command
./gradlew jar -PscalaVersion=2.13.15


KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
export KAFKA_CLUSTER_ID=$(bin/kafka-storage.sh random-uuid)

bin/kafka-topics.sh --create --topic order_topics --bootstrap-server localhost:9092


bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties

bin/kafka-server-start.sh config/server.properties

bin/kafka-topics.sh --create --topic order_topics --bootstrap-server localhost:9092

bin/kafka-console-producer.sh --topic order_topics --bootstrap-server localhost:9092

bin/kafka-console-consumer.sh --topic order_topics --from-beginning --bootstrap-server localhost:9092




✅ Option 1: Reset the Kafka consumer offset to skip old bad messages
Since the consumer group stock is currently trying to read from offset 0 (where bad text messages exist), you can reset it to the latest valid offset using this command:
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
--group stock \
--topic order_topics \
--reset-offsets --to-latest --execute