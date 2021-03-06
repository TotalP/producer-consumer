package org.total.drive;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.total.constants.Utils;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Pavlo.Fandych
 */
public final class KafkaProducerTestDrive {

    private static final Logger LOGGER = Logger.getLogger(KafkaProducerTestDrive.class);

    public static void main(String[] args) {

        long start = System.nanoTime();

        try (final Producer<String, String> producer = createProducer()) {
            for (int i = 0; i < Utils.TIMES; ++i) {
                final ProducerRecord<String, String> recordToSend = new ProducerRecord<>(Utils.TOPIC_NAME, "key",
                        "TEST MESSAGE " + i);
                producer.send(recordToSend).get();
                producer.flush();
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error(e, e);
        }

        long end = System.nanoTime();

        try (final Producer<String, String> producer = createProducer()) {

            final ProducerRecord<String, String> recordToSend = new ProducerRecord<>(Utils.TOPIC_NAME, "key",
                    "Send duration " + (start - end));
            producer.send(recordToSend).get();
            producer.flush();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error(e, e);
        }
    }

    private static Producer<String, String> createProducer() {
        final Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<>(kafkaProps);
    }
}
