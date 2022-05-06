package org.acme;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.netty.util.concurrent.Future;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;


@Startup
@ApplicationScoped
public class GreetingResource {
    private static final Logger logger = LoggerFactory.getLogger(GreetingResource.class);
    public static String brokers = System.getenv("BROKER_URL") ==null ? "redpandaserver:9092" : System.getenv("BROKER_URL");
    public static String topic = System.getenv("TOPIC") ==null ? "nuthan" : System.getenv("TOPIC");

   
    void onStart(@Observes StartupEvent ev)  {
        Properties properties = new Properties();
		properties.put("bootstrap.servers", brokers);
		properties.put("acks", "0");
		properties.put("retries", "1");
		properties.put("batch.size", "20971520");
		properties.put("linger.ms", "33");
		properties.put("max.request.size", "2097152");
		properties.put("compression.type", "gzip");
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		System.out.println(brokers);
            KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
            try {
                for(int i = 0; i < 10;i++){
                producer.send(new ProducerRecord<String, String>(topic, "key -->"+Instant.now(), "hello  " +Instant.now()));
                producer.flush();
                System.out.println("sending message"+i);
                }
            } finally {
                producer.close();
            }
    }
    void onStop(@Observes ShutdownEvent ev) {
        logger.info("The Simulator is stopping...");
    }
}


// public class GreetingResource {

//     public static void main(String[] args) {
    // public static String brokers = System.getenv("BROKER_URL") =="" ? "localhost:9092" : System.getenv("BROKER_URL");
    // public static String topic = System.getenv("TOPIC") =="" ? "nuthan" : System.getenv("TOPIC");
//         Properties props = new Properties();
//         props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//         props.put(ProducerConfig.ACKS_CONFIG, "all");
//         props.put(ProducerConfig.RETRIES_CONFIG, 0);
//         props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//         props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

//             Producer<String, String> producer = new KafkaProducer<>(props);
//             for (int i = 0; i < 100; i++)
//                 producer.send(new ProducerRecord<String, String>("nuthan", Integer.toString(i), Integer.toString(i)));
           
//             producer.close();
//     }
    


// }
