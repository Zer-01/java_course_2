package edu.java.bot.configuration.kafka;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.listeners.UpdateListener;
import edu.java.bot.service.UpdateProcessService;
import jakarta.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "app", name = "enable-kafka", havingValue = "true")
@EnableKafka
public class KafkaListenerConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, LinkUpdateRequest>>
    kafkaListenerContainerFactory(
        KafkaTemplate<String, LinkUpdateRequest> template
    ) {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
            template,
            (rec, ex) -> new TopicPartition(applicationConfig.kafka().dlqTopic(), rec.partition())
        );

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer);
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, LinkUpdateRequest> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
            consumerConfigs(),
            new ErrorHandlingDeserializer<>(new StringDeserializer()),
            new ErrorHandlingDeserializer<>(new JsonDeserializer<>(LinkUpdateRequest.class))
        );
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfig.kafka().bootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, applicationConfig.kafka().groupId());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "edu.java.api.models");
        return props;
    }

    @Bean
    public UpdateListener updateListener(UpdateProcessService updateProcessService, Validator validator) {
        return new UpdateListener(updateProcessService, validator);
    }
}
