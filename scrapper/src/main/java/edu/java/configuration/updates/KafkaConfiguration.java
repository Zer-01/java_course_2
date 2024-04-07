package edu.java.configuration.updates;

import java.util.HashMap;
import java.util.Map;
import edu.java.api.models.LinkUpdateRequest;
import edu.java.configuration.ApplicationConfig;
import edu.java.producers.ScrapperQueueProducer;
import edu.java.service.SendUpdateService;
import edu.java.service.senders.KafkaSendUpdateService;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class KafkaConfiguration {
    private final ApplicationConfig applicationConfig;

    @Autowired
    public KafkaConfiguration(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfig.kafka().bootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic updatesTopic() {
        return TopicBuilder.name(applicationConfig.kafka().updatesTopic())
            .partitions(applicationConfig.kafka().topicPartitions())
            .replicas(applicationConfig.kafka().replicas())
            .build();
    }

    @Bean
    public ProducerFactory<String, LinkUpdateRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(senderProps());
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfig.kafka().bootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate(ProducerFactory<String, LinkUpdateRequest> prodFact) {
        return new KafkaTemplate<>(prodFact);
    }

    @Bean
    public ScrapperQueueProducer scrapperQueueProducer(KafkaTemplate<String, LinkUpdateRequest> template) {
        return new ScrapperQueueProducer(template);
    }

    @Bean
    public SendUpdateService sendUpdateService(ScrapperQueueProducer producer) {
        return new KafkaSendUpdateService(producer, applicationConfig.kafka().updatesTopic());
    }
}
