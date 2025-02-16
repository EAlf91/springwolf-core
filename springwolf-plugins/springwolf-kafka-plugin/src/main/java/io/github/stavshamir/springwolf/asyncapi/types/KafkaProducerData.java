package io.github.stavshamir.springwolf.asyncapi.types;

import com.asyncapi.v2.binding.kafka.KafkaChannelBinding;
import com.asyncapi.v2.binding.kafka.KafkaOperationBinding;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;

public class KafkaProducerData extends ProducerData {

    @Builder(builderMethodName = "kafkaProducerDataBuilder")
    public KafkaProducerData(String topicName, Class<?> payloadType, String description) {
        this.channelName = topicName;
        this.description = description;
        this.channelBinding = ImmutableMap.of("kafka", new KafkaChannelBinding());
        this.payloadType = payloadType;
        this.operationBinding = ImmutableMap.of("kafka", new KafkaOperationBinding());
    }

}
