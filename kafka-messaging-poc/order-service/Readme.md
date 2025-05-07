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


🔹 1. Partitions
A partition is a basic unit of parallelism and scalability in Kafka.

Each Kafka topic is split into one or more partitions.

A partition is an ordered, immutable sequence of records.

Kafka distributes partitions across brokers to parallelize processing.

Example:
If you create a topic with 3 partitions and produce 9 messages, Kafka will distribute them among those 3 partitions (e.g., round-robin by default or based on a key).

✅ More partitions = more consumers can consume in parallel
⚠️ But each partition can be read by only one consumer in a group at a time.

🔹 2. Replicas
Replicas provide fault-tolerance. A replica is a copy of a partition stored on another Kafka broker.

One replica is the leader, and others are followers.

If the leader broker crashes, a follower takes over.

This ensures high availability of data.

Example:
If you create a topic with 3 partitions and 2 replicas, Kafka will ensure:

Each partition is stored on 2 brokers (1 leader + 1 replica).

If the leader broker dies, the follower continues serving the data.

✅ More replicas = better fault tolerance
⚠️ But requires more broker resources

📌 TL;DR

Property	Meaning	Benefit
partitions	Number of logs per topic (parallelism)	Improves performance & scaling
replicas	Number of copies of each partition	Ensures high availability